package com.sinosoft.one.data.jpa.repository.config;

import com.sinosoft.one.data.jpa.repository.query.PropertiesBasedSqlQueries;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.data.repository.core.support.PropertiesBasedNamedQueries;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * User: Morgan
 * Date: 12-8-15
 * Time: 下午4:09
 */
public class SqlQueriesBeanDefinitionParser implements BeanDefinitionParser {


	private static final String ATTRIBUTE = "sql-queries-location";
	private final String defaultLocation = "classpath*:META-INF/sql-queries.properties";

	/**
	 * Creates a new {@link SqlQueriesBeanDefinitionParser} using the given default location.
	 *
	 */
	public SqlQueriesBeanDefinitionParser() {
		//Assert.hasText(defaultLocation);
		//this.defaultLocation = defaultLocation;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.BeanDefinitionParser#parse(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
	 */
	public BeanDefinition parse(Element element, ParserContext parserContext) {

		BeanDefinitionBuilder properties = BeanDefinitionBuilder.rootBeanDefinition(PropertiesFactoryBean.class);
		properties.addPropertyValue("locations", getDefaultedLocation(element));

		if (isDefaultLocation(element)) {
			properties.addPropertyValue("ignoreResourceNotFound", true);
		}

		AbstractBeanDefinition propertiesDefinition = properties.getBeanDefinition();
		propertiesDefinition.setSource(parserContext.extractSource(element));

		BeanDefinitionBuilder sqlQueries = BeanDefinitionBuilder.rootBeanDefinition(PropertiesBasedSqlQueries.class);
		sqlQueries.addConstructorArgValue(propertiesDefinition);

		AbstractBeanDefinition namedQueriesDefinition = sqlQueries.getBeanDefinition();
		namedQueriesDefinition.setSource(parserContext.extractSource(element));

		return namedQueriesDefinition;
	}

	/**
	 * Returns whether we should use the default location.
	 *
	 * @param element
	 * @return
	 */
	private boolean isDefaultLocation(Element element) {
		return !StringUtils.hasText(element.getAttribute(ATTRIBUTE));
	}

	/**
	 * Returns the location to look for {@link java.util.Properties} if configured or the default one if not.
	 *
	 * @param element
	 * @return
	 */
	private String getDefaultedLocation(Element element) {

		String locations = element.getAttribute(ATTRIBUTE);
		return StringUtils.hasText(locations) ? locations : defaultLocation;
	}
}
