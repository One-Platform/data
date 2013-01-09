/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sinosoft.one.data.jpa.repository.support;

import com.sinosoft.one.data.jpa.repository.query.SqlQueries;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import com.sinosoft.one.data.jpa.repository.query.OneQueryLookupStrategy.Key;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * Adapter for Springs {@link org.springframework.beans.factory.FactoryBean} interface to allow easy setup of repository factories via Spring
 * configuration.
 *
 * @param <T> the type of the repository
 * @author Oliver Gierke
 */
public abstract class OneRepositoryFactoryBeanSupport<T extends Repository<S, ID>, S, ID extends Serializable> implements
		InitializingBean, RepositoryFactoryInformation<S, ID>, FactoryBean<T> {

	private OneRepositoryFactorySupport factory;

	private Key queryLookupStrategyKey;
	private Class<? extends T> repositoryInterface;
	private Object customImplementation;
	private NamedQueries namedQueries;
	protected SqlQueries sqlQueries;

	/**
	 * Setter to inject the repository interface to implement.
	 *
	 * @param repositoryInterface the repository interface to set
	 */
	@Required
	public void setRepositoryInterface(Class<? extends T> repositoryInterface) {

		Assert.notNull(repositoryInterface);
		this.repositoryInterface = repositoryInterface;
	}

	/**
	 * Set the {@link org.springframework.data.repository.query.QueryLookupStrategy.Key} to be used.
	 *
	 * @param queryLookupStrategyKey
	 */
	public void setQueryLookupStrategyKey(Key queryLookupStrategyKey) {

		this.queryLookupStrategyKey = queryLookupStrategyKey;
	}

	/**
	 * Setter to inject a custom repository implementation.
	 *
	 * @param customImplementation
	 */
	public void setCustomImplementation(Object customImplementation) {

		this.customImplementation = customImplementation;
	}

	/**
	 * Setter to inject a {@link org.springframework.data.repository.core.NamedQueries} instance.
	 *
	 * @param namedQueries the namedQueries to set
	 */
	public void setNamedQueries(NamedQueries namedQueries) {
		this.namedQueries = namedQueries;
	}

	public void setSqlQueries(SqlQueries sqlQueries) {
		this.sqlQueries = sqlQueries;
	}



	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryInformation#getEntityInformation()
	 */
	@SuppressWarnings("unchecked")
	public EntityInformation<S, ID> getEntityInformation() {

		RepositoryMetadata repositoryMetadata = factory.getRepositoryMetadata(repositoryInterface);
		return (EntityInformation<S, ID>) factory.getEntityInformation(repositoryMetadata.getDomainType());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryInformation#getRepositoryInformation()
	 */
	public RepositoryInformation getRepositoryInformation() {
		RepositoryMetadata metadata = factory.getRepositoryMetadata(repositoryInterface);
		return this.factory.getRepositoryInformation(metadata,
				customImplementation == null ? null : customImplementation.getClass());
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryInformation#getQueryMethods()
	 */
	public List<QueryMethod> getQueryMethods() {
		return factory.getQueryMethods();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public T getObject() {
		return factory.getRepository(repositoryInterface, customImplementation);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends T> getObjectType() {
		return (Class<? extends T>) (null == repositoryInterface ? Repository.class : repositoryInterface);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {

		this.factory = createRepositoryFactory();
		this.factory.setQueryLookupStrategyKey(queryLookupStrategyKey);
		this.factory.setNamedQueries(namedQueries);
		this.factory.setSqlQueries(sqlQueries);
	}

	/**
	 * Create the actual {@link OneRepositoryFactorySupport} instance.
	 * 
	 * @return
	 */
	protected abstract OneRepositoryFactorySupport createRepositoryFactory();
}
