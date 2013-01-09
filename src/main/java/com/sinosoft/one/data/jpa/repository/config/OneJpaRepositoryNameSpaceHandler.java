package com.sinosoft.one.data.jpa.repository.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.data.jpa.repository.config.AuditingBeanDefinitionParser;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-8-6
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
public class OneJpaRepositoryNameSpaceHandler extends NamespaceHandlerSupport {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public void init() {
        registerBeanDefinitionParser("repositories", new OneJpaRepositoryConfigDefinitionParser());
        registerBeanDefinitionParser("auditing", new AuditingBeanDefinitionParser());
    }
}
