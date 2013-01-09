package com.sinosoft.one.data.jpa.repository.config;

import com.sinosoft.one.data.jpa.repository.query.OneQueryLookupStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.config.GlobalRepositoryConfigInformation;
import org.springframework.data.repository.config.NamedQueriesBeanDefinitionParser;
import org.springframework.data.repository.config.SingleRepositoryConfigInformation;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.springframework.beans.factory.support.BeanDefinitionReaderUtils.GENERATED_BEAN_NAME_SEPARATOR;
import static org.springframework.beans.factory.support.BeanDefinitionReaderUtils.generateBeanName;
import static org.springframework.data.repository.util.ClassUtils.isGenericRepositoryInterface;

/**
 * User: Morgan
 * Date: 12-8-15
 * Time: 下午4:24
 */
public abstract class OneAbstractRepositoryConfigDefinitionParser<S extends GlobalRepositoryConfigInformation<T>, T extends SingleRepositoryConfigInformation<S>>
		implements BeanDefinitionParser {
	private static final Log LOG = LogFactory.getLog(OneAbstractRepositoryConfigDefinitionParser.class);

	private static final String REPOSITORY_INTERFACE_POST_PROCESSOR = "com.sinosoft.one.data.jpa.repository.support.OneRepositoryInterfaceAwareBeanPostProcessor";

	/**
	 * 定义QUERY_LOOKUP_STRATEGY 为了解决 OneQueryLookupStrategy.key 与 QueryLookupStrategy.key 不匹配
	 */
	public static final String QUERY_LOOKUP_STRATEGY = "query-lookup-strategy";


	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.BeanDefinitionParser#parse(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
	 */
	public BeanDefinition parse(Element element, ParserContext parser) {

		try {
			S configContext = getGlobalRepositoryConfigInformation(element);

			if (configContext.configureManually()) {
				doManualConfiguration(configContext, parser);
			} else {
				doAutoConfiguration(configContext, parser);
			}

			Object beanSource = parser.extractSource(element);
			registerBeansForRoot(parser.getRegistry(), beanSource);

		} catch (RuntimeException e) {
			handleError(e, element, parser.getReaderContext());
		}

		return null;
	}

	/**
	 * Executes repository auto configuration by scanning the provided base package for repository interfaces.
	 *
	 * @param config
	 * @param parser
	 */
	private void doAutoConfiguration(S config, ParserContext parser) {

		LOG.debug("Triggering auto repository detection");

		ResourceLoader resourceLoader = parser.getReaderContext().getResourceLoader();

		// Detect available repository interfaces
		Set<String> repositoryInterfaces = getRepositoryInterfacesForAutoConfig(config, resourceLoader,
				parser.getReaderContext());

		for (String repositoryInterface : repositoryInterfaces) {
			registerGenericRepositoryFactoryBean(parser, config.getAutoconfigRepositoryInformation(repositoryInterface));
		}
	}

	private Set<String> getRepositoryInterfacesForAutoConfig(S config, ResourceLoader loader, ReaderContext reader) {

		ClassPathScanningCandidateComponentProvider scanner = new RepositoryComponentProvider(
				config.getRepositoryBaseInterface());
		scanner.setResourceLoader(loader);

		TypeFilterParser parser = new TypeFilterParser(loader.getClassLoader(), reader);
		parser.parseFilters(config.getSource(), scanner);

		Set<BeanDefinition> findCandidateComponents = scanner.findCandidateComponents(config.getBasePackage());

		Set<String> interfaceNames = new HashSet<String>();
		for (BeanDefinition definition : findCandidateComponents) {
			interfaceNames.add(definition.getBeanClassName());
		}

		return interfaceNames;
	}

	/**
	 * Returns a {@link GlobalRepositoryConfigInformation} implementation for the given element.
	 *
	 * @param element
	 * @return
	 */
	protected abstract S getGlobalRepositoryConfigInformation(Element element);

	/**
	 * Proceeds manual configuration by traversing the context's {@link SingleRepositoryConfigInformation}s.
	 *
	 * @param context
	 * @param parser
	 */
	private void doManualConfiguration(S context, ParserContext parser) {

		LOG.debug("Triggering manual repository detection");

		for (T repositoryContext : context.getSingleRepositoryConfigInformations()) {
			registerGenericRepositoryFactoryBean(parser, repositoryContext);
		}
	}

	private void handleError(Exception e, Element source, ReaderContext reader) {

		reader.error(e.getMessage(), reader.extractSource(source), e.getCause());
	}

	/**
	 * Registers a generic repository factory bean for a bean with the given name and the provided configuration context.
	 *
	 * @param parser
	 * @param context
	 */
	private void registerGenericRepositoryFactoryBean(ParserContext parser, T context) {

		try {

			Object beanSource = parser.extractSource(context.getSource());

			BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(context
					.getRepositoryFactoryBeanClassName());

			builder.addPropertyValue("repositoryInterface", context.getInterfaceName());

			//builder.addPropertyValue("queryLookupStrategyKey", context.getQueryLookupStrategyKey());
			//2012-8-30 修改为
			builder.addPropertyValue("queryLookupStrategyKey", getOneQueryLookupStrategyKey(context.getSource()));

			builder.addPropertyValue("namedQueries",
					new NamedQueriesBeanDefinitionParser(context.getNamedQueriesLocation()).parse(context.getSource(), parser));
			//添加sqlQueries
			builder.addPropertyValue("sqlQueries",
						new SqlQueriesBeanDefinitionParser()
						.parse(context.getSource(),parser));

			String customImplementationBeanName = registerCustomImplementation(context, parser, beanSource);

			if (customImplementationBeanName != null) {
				builder.addPropertyReference("customImplementation", customImplementationBeanName);
			}

			postProcessBeanDefinition(context, builder, parser.getRegistry(), beanSource);

			AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
			beanDefinition.setSource(beanSource);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Registering repository: " + context.getBeanId() + " - Interface: " + context.getInterfaceName()
						+ " - Factory: " + context.getRepositoryFactoryBeanClassName() + ", - Custom implementation: "
						+ customImplementationBeanName);
			}

			BeanComponentDefinition definition = new BeanComponentDefinition(beanDefinition, context.getBeanId());
			parser.registerBeanComponent(definition);
		} catch (RuntimeException e) {
			handleError(e, context.getSource(), parser.getReaderContext());
		}
	}

	/**
	 *	此方法为了解决 OneQueryLookupStrategy.Key 和 QueryLookupStrategy.key 不匹配
	 * @param element
	 * @return
	 */
	private OneQueryLookupStrategy.Key getOneQueryLookupStrategyKey( Element element ) {
		String createFinderQueries = element.getAttribute(QUERY_LOOKUP_STRATEGY);

		return StringUtils.hasText(createFinderQueries) ? OneQueryLookupStrategy.Key.create(createFinderQueries) : null;
	}

	/**
	 * Callback to post process a repository bean definition prior to actual registration.
	 *
	 * @param context
	 * @param builder
	 * @param beanSource
	 */
	protected void postProcessBeanDefinition(T context, BeanDefinitionBuilder builder, BeanDefinitionRegistry registry,
											 Object beanSource) {

	}

	/**
	 * Registers a possibly available custom repository implementation on the repository bean. Tries to find an already
	 * registered bean to reference or tries to detect a custom implementation itself.
	 *
	 * @param config
	 * @param parser
	 * @param source
	 * @return the bean name of the custom implementation or {@code null} if none available
	 */
	private String registerCustomImplementation(T config, ParserContext parser, Object source) {

		String beanName = config.getImplementationBeanName();

		// Already a bean configured?
		if (parser.getRegistry().containsBeanDefinition(beanName)) {
			return beanName;
		}

		// Autodetect implementation
		if (config.autodetectCustomImplementation()) {

			AbstractBeanDefinition beanDefinition = detectCustomImplementation(config, parser);

			if (null == beanDefinition) {
				return null;
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("Registering custom repository implementation: " + config.getImplementationBeanName() + " "
						+ beanDefinition.getBeanClassName());
			}

			beanDefinition.setSource(source);
			parser.registerBeanComponent(new BeanComponentDefinition(beanDefinition, beanName));

		} else {
			beanName = config.getCustomImplementationRef();
		}

		return beanName;
	}

	/**
	 * Tries to detect a custom implementation for a repository bean by classpath scanning.
	 *
	 * @param config
	 * @param parser
	 * @return the {@code AbstractBeanDefinition} of the custom implementation or {@literal null} if none found
	 */
	private AbstractBeanDefinition detectCustomImplementation(T config, ParserContext parser) {

		// Build pattern to lookup implementation class
		Pattern pattern = Pattern.compile(".*\\." + config.getImplementationClassName());

		// Build classpath scanner and lookup bean definition
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.setResourceLoader(parser.getReaderContext().getResourceLoader());
		provider.addIncludeFilter(new RegexPatternTypeFilter(pattern));
		Set<BeanDefinition> definitions = provider.findCandidateComponents(config.getBasePackage());

		if (definitions.size() == 0) {
			return null;
		}

		if (definitions.size() == 1) {
			return (AbstractBeanDefinition) definitions.iterator().next();
		}

		List<String> implementationClassNames = new ArrayList<String>();
		for (BeanDefinition bean : definitions) {
			implementationClassNames.add(bean.getBeanClassName());
		}

		throw new IllegalStateException(String.format(
				"Ambiguous custom implementations detected! Found %s but expected a single implementation!",
				StringUtils.collectionToCommaDelimitedString(implementationClassNames)));
	}

	/**
	 * Callback to register additional bean definitions for a {@literal repositories} root node. This usually includes
	 * beans you have to set up once independently of the number of repositories to be created. Will be called before any
	 * repositories bean definitions have been registered.
	 *
	 * @param registry
	 * @param source
	 */
	protected void registerBeansForRoot(BeanDefinitionRegistry registry, Object source) {

		AbstractBeanDefinition definition = BeanDefinitionBuilder.rootBeanDefinition(REPOSITORY_INTERFACE_POST_PROCESSOR)
				.getBeanDefinition();

		registerWithSourceAndGeneratedBeanName(registry, definition, source);
	}

	/**
	 * Returns whether the given {@link BeanDefinitionRegistry} already contains a bean of the given type assuming the
	 * bean name has been autogenerated.
	 *
	 * @param type
	 * @param registry
	 * @return
	 */
	protected static boolean hasBean(Class<?> type, BeanDefinitionRegistry registry) {

		String name = String.format("%s%s0", type.getName(), GENERATED_BEAN_NAME_SEPARATOR);
		return registry.containsBeanDefinition(name);
	}

	/**
	 * Sets the given source on the given {@link AbstractBeanDefinition} and registers it inside the given
	 * {@link BeanDefinitionRegistry}.
	 *
	 * @param registry
	 * @param bean
	 * @param source
	 * @return
	 */
	protected static String registerWithSourceAndGeneratedBeanName(BeanDefinitionRegistry registry,
																   AbstractBeanDefinition bean, Object source) {

		bean.setSource(source);

		String beanName = generateBeanName(bean, registry);
		registry.registerBeanDefinition(beanName, bean);

		return beanName;
	}

	/**
	 * Custom {@link ClassPathScanningCandidateComponentProvider} scanning for interfaces extending the given base
	 * interface. Skips interfaces annotated with {@link org.springframework.data.repository.NoRepositoryBean}.
	 *
	 * @author Oliver Gierke
	 */
	static class RepositoryComponentProvider extends ClassPathScanningCandidateComponentProvider {

		/**
		 * Creates a new {@link RepositoryComponentProvider}.
		 *
		 * @param repositoryInterface the interface to scan for
		 */
		public RepositoryComponentProvider(Class<?> repositoryInterface) {

			super(false);
			addIncludeFilter(new InterfaceTypeFilter(repositoryInterface));
			addIncludeFilter(new AnnotationTypeFilter(RepositoryDefinition.class, true, true));
			addExcludeFilter(new AnnotationTypeFilter(NoRepositoryBean.class));
		}

		/*
						 * (non-Javadoc)
						 *
						 * @seeorg.springframework.context.annotation.
						 * ClassPathScanningCandidateComponentProvider
						 * #isCandidateComponent(org.springframework
						 * .beans.factory.annotation.AnnotatedBeanDefinition)
						 */
		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {

			boolean isNonRepositoryInterface = !isGenericRepositoryInterface(beanDefinition.getBeanClassName());
			boolean isTopLevelType = !beanDefinition.getMetadata().hasEnclosingClass();

			return isNonRepositoryInterface && isTopLevelType;
		}

		/**
		 * {@link org.springframework.core.type.filter.TypeFilter} that only matches interfaces. Thus setting this up makes
		 * only sense providing an interface type as {@code targetType}.
		 *
		 * @author Oliver Gierke
		 */
		private static class InterfaceTypeFilter extends AssignableTypeFilter {

			/**
			 * Creates a new {@link InterfaceTypeFilter}.
			 *
			 * @param targetType
			 */
			public InterfaceTypeFilter(Class<?> targetType) {

				super(targetType);
			}

			/*
									 * (non-Javadoc)
									 *
									 * @seeorg.springframework.core.type.filter.
									 * AbstractTypeHierarchyTraversingFilter
									 * #match(org.springframework.core.type.classreading.MetadataReader,
									 * org.springframework.core.type.classreading.MetadataReaderFactory)
									 */
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
					throws IOException {

				return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
			}
		}

		// Copy of Spring's AnnotationTypeFilter until SPR-8336 gets resolved.

		/**
		 * A simple filter which matches classes with a given annotation, checking inherited annotations as well.
		 *
		 * <p>
		 * The matching logic mirrors that of <code>Class.isAnnotationPresent()</code>.
		 *
		 * @author Mark Fisher
		 * @author Ramnivas Laddad
		 * @author Juergen Hoeller
		 * @since 2.5
		 */
		private static class AnnotationTypeFilter extends AbstractTypeHierarchyTraversingFilter {

			private final Class<? extends Annotation> annotationType;

			private final boolean considerMetaAnnotations;

			/**
			 * Create a new AnnotationTypeFilter for the given annotation type. This filter will also match meta-annotations.
			 * To disable the meta-annotation matching, use the constructor that accepts a '
			 * <code>considerMetaAnnotations</code>' argument. The filter will not match interfaces.
			 *
			 * @param annotationType the annotation type to match
			 */
			public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
				this(annotationType, true);
			}

			/**
			 * Create a new AnnotationTypeFilter for the given annotation type. The filter will not match interfaces.
			 *
			 * @param annotationType the annotation type to match
			 * @param considerMetaAnnotations whether to also match on meta-annotations
			 */
			public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
				this(annotationType, considerMetaAnnotations, false);
			}

			/**
			 * Create a new {@link AnnotationTypeFilter} for the given annotation type.
			 *
			 * @param annotationType the annotation type to match
			 * @param considerMetaAnnotations whether to also match on meta-annotations
			 * @param considerInterfaces whether to also match interfaces
			 */
			public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations,
										boolean considerInterfaces) {
				super(annotationType.isAnnotationPresent(Inherited.class), considerInterfaces);
				this.annotationType = annotationType;
				this.considerMetaAnnotations = considerMetaAnnotations;
			}

			@Override
			protected boolean matchSelf(MetadataReader metadataReader) {
				AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
				return metadata.hasAnnotation(this.annotationType.getName())
						|| (this.considerMetaAnnotations && metadata.hasMetaAnnotation(this.annotationType.getName()));
			}

			@Override
			protected Boolean matchSuperClass(String superClassName) {
				if (Object.class.getName().equals(superClassName)) {
					return Boolean.FALSE;
				} else if (superClassName.startsWith("java.")) {
					try {
						Class<?> clazz = getClass().getClassLoader().loadClass(superClassName);
						return (clazz.getAnnotation(this.annotationType) != null);
					} catch (ClassNotFoundException ex) {
						// Class not found - can't determine a match that way.
					}
				}
				return null;
			}
		}
	}
}
