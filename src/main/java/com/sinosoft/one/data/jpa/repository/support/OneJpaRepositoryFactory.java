/*
 * Copyright 2008-2011 the original author or authors.
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

import com.sinosoft.one.data.jpa.repository.query.OneJpaQueryLookupStrategy;
import com.sinosoft.one.data.jpa.repository.query.OneQueryLookupStrategy;
import com.sinosoft.one.data.jpa.repository.query.OneQueryLookupStrategy.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.QueryExtractor;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

/**
 * JPA specific generic repository factory.
 * 
 * @author Oliver Gierke
 */
public class OneJpaRepositoryFactory extends OneRepositoryFactorySupport {

	private final EntityManager entityManager;
	private final QueryExtractor extractor;
	private final LockModeRepositoryPostProcessor lockModePostProcessor;

	/**
	 * Creates a new {@link OneJpaRepositoryFactory}.
	 *
	 * @param entityManager must not be {@literal null}
	 */
	public OneJpaRepositoryFactory(EntityManager entityManager) {

		Assert.notNull(entityManager);
		this.entityManager = entityManager;
		this.extractor = PersistenceProvider.fromEntityManager(entityManager);
		this.lockModePostProcessor = LockModeRepositoryPostProcessor.INSTANCE;

		addRepositoryProxyPostProcessor(lockModePostProcessor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.data.repository.support.RepositoryFactorySupport#
	 * getTargetRepository(java.lang.Class)
	 */

	protected Object getTargetRepository(RepositoryMetadata metadata) {

		return getTargetRepository(metadata, entityManager);
	}

	/**
	 * Callback to create a {@link org.springframework.data.jpa.repository.JpaRepository} instance with the given {@link javax.persistence.EntityManager}
	 *
	 * @param <T>
	 * @param <ID>
	 * @param entityManager
	 * @see #getTargetRepository(org.springframework.data.repository.core.RepositoryMetadata)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata,
			EntityManager entityManager) {

		Class<?> repositoryInterface = metadata.getRepositoryInterface();
		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

		SimpleJpaRepository<?, ?> repo = isQueryDslExecutor(repositoryInterface) ? new QueryDslJpaRepository(
				entityInformation, entityManager) : new SimpleJpaRepository(entityInformation, entityManager);
		repo.setLockMetadataProvider(lockModePostProcessor.getLockMetadataProvider());

		return repo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.support.RepositoryFactorySupport#
	 * getRepositoryBaseClass()
	 */

	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

		if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
			return QueryDslJpaRepository.class;
		} else {
			return SimpleJpaRepository.class;
		}
	}

	/**
	 * Returns whether the given repository interface requires a QueryDsl specific implementation to be chosen.
	 * 
	 * @param repositoryInterface
	 * @return
	 */
	private boolean isQueryDslExecutor(Class<?> repositoryInterface) {

		return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.support.RepositoryFactorySupport#
	 * getQueryLookupStrategy
	 * (org.springframework.data.repository.query.QueryLookupStrategy.Key)
	 */

	protected OneQueryLookupStrategy getQueryLookupStrategy(Key key) {

		return OneJpaQueryLookupStrategy.create(entityManager, key, extractor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.support.RepositoryFactorySupport#
	 * getEntityInformation(java.lang.Class)
	 */

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> JpaEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {

		return (JpaEntityInformation<T, ID>) JpaEntityInformationSupport.getMetadata(domainClass, entityManager);
	}
}
