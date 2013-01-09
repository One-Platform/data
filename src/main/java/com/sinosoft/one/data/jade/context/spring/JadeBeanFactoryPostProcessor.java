package com.sinosoft.one.data.jade.context.spring;

import com.sinosoft.one.data.jade.rowmapper.DefaultRowMapperFactory;
import com.sinosoft.one.data.jade.rowmapper.RowMapperFactory;
import com.sinosoft.one.data.jade.statement.InterpreterFactory;
import com.sinosoft.one.data.jade.statement.cached.CacheProvider;
import com.sinosoft.one.data.jpa.repository.support.OneJpaRepositoryFactoryBean;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-8-13
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class JadeBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    /**
     * 开关属性前缀常量
     */
    private static final String propertyPrefix = "jade.context.spring";;

    /**
     * 日志记录器
     */
    private final Log logger = LogFactory.getLog(JadeBeanFactoryPostProcessor.class);

    /**
     * 行映射器工厂，通过它可以得到一个数据库表的行映射器实现，使得从数据库读取的记录可以映射为一个对象
     * <p>
     *
     * @see #getRowMapperFactory()
     */
    private RowMapperFactory rowMapperFactory;

    /**
     * 解释器工厂，通过它可以或得一个DAO方法对应的解析器数组，这些解析器数组将解析每一次DAO操作，进行SQL解析或设置运行时状态
     * <p>
     *
     * @see #getInterpreterFactory(ConfigurableListableBeanFactory)
     */
    private InterpreterFactory interpreterFactory;

    /**
     * 缓存提供者的bean名称，为“none”等价于null
     */
    private String cacheProviderName;


    public InterpreterFactory getInterpreterFactory(ConfigurableListableBeanFactory beanFactory) {
        if (interpreterFactory == null) {
            interpreterFactory = new SpringInterpreterFactory(beanFactory);
        }
        return interpreterFactory;
    }

    public RowMapperFactory getRowMapperFactory() {
        if (rowMapperFactory == null) {
            rowMapperFactory = new DefaultRowMapperFactory();
        }
        return rowMapperFactory;
    }

    public String getCacheProviderName(ConfigurableListableBeanFactory beanFactory) {
        if (cacheProviderName == null) {
            String[] names = beanFactory.getBeanNamesForType(CacheProvider.class);
            if (names.length == 0) {
                cacheProviderName = "none";
            } else if (names.length == 1) {
                cacheProviderName = names[0];
            } else {
                String topPriority = "jade.cacheProvider";
                if (ArrayUtils.contains(names, topPriority)) {
                    cacheProviderName = topPriority;
                } else {
                    throw new IllegalStateException(
                            "required not more than 1 CacheProvider, but found " + names.length);
                }
            }
        }
        return "none".equals(cacheProviderName) ? null : cacheProviderName;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanNamesForType(OneJpaRepositoryFactoryBean.class, true, false);
        for(String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName.substring(1));

            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
            propertyValues.addPropertyValue("rowMapperFactory", getRowMapperFactory());
            propertyValues.addPropertyValue("interpreterFactory", getInterpreterFactory(beanFactory));
            String cacheProviderName = getCacheProviderName(beanFactory);
            if (cacheProviderName != null) {
                RuntimeBeanReference beanRef = new RuntimeBeanReference(cacheProviderName);
                propertyValues.addPropertyValue("cacheProvider", beanRef);
            }
        }
    }
}
