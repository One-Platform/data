package org.springframework.data.repository.core.support;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-8-13
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public final class SpringDataJpaCoreSupportAdapter {
    private SpringDataJpaCoreSupportAdapter() {

    }
    private static SpringDataJpaCoreSupportAdapter instance = new SpringDataJpaCoreSupportAdapter();
    public static SpringDataJpaCoreSupportAdapter getInstance() {
        return instance;
    }
    public DefaultRepositoryInformation newDefaultRepositoryInformation(RepositoryMetadata metadata, Class<?> repositoryBaseClass,
                                                                  Class<?> customImplementationClass) {
        return new DefaultRepositoryInformation(metadata, repositoryBaseClass, customImplementationClass);
    }
}
