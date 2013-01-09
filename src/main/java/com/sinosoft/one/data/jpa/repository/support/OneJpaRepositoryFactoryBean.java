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

import com.sinosoft.one.data.jade.context.JadeInvocationHandler;
import com.sinosoft.one.data.jade.dataaccess.DataAccessFactory;
import com.sinosoft.one.data.jade.rowmapper.RowMapperFactory;
import com.sinosoft.one.data.jade.statement.DAOMetaData;
import com.sinosoft.one.data.jade.statement.InterpreterFactory;
import com.sinosoft.one.data.jade.statement.cached.CacheProvider;
import org.springframework.data.repository.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Special adapter for Springs {@link org.springframework.beans.factory.FactoryBean} interface to allow easy setup of
 * repository factories via Spring configuration.
 * 
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @param <T> the type of the repository
 */
public class OneJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		OneTransactionalRepositoryFactoryBeanSupport<T, S, ID> {

	private EntityManager entityManager;

    protected DataAccessFactory dataAccessFactory;

    protected RowMapperFactory rowMapperFactory;

    protected InterpreterFactory interpreterFactory;

    protected CacheProvider cacheProvider;

    public DataAccessFactory getDataAccessFactory() {
        return dataAccessFactory;
    }

    public void setDataAccessFactory(DataAccessFactory dataAccessFactory) {
        this.dataAccessFactory = dataAccessFactory;
    }

    public RowMapperFactory getRowMapperFactory() {
        return rowMapperFactory;
    }

    public void setRowMapperFactory(RowMapperFactory rowMapperFactory) {
        this.rowMapperFactory = rowMapperFactory;
    }

    public InterpreterFactory getInterpreterFactory() {
        return interpreterFactory;
    }

    public void setInterpreterFactory(InterpreterFactory interpreterFactory) {
        this.interpreterFactory = interpreterFactory;
    }

    public CacheProvider getCacheProvider() {
        return cacheProvider;
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    /**
	 * The {@link javax.persistence.EntityManager} to be used.
	 *
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {

		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.data.repository.support.
	 * TransactionalRepositoryFactoryBeanSupport#doCreateRepositoryFactory()
	 */

	protected OneRepositoryFactorySupport doCreateRepositoryFactory() {
        OneRepositoryFactorySupport oneRepositoryFactorySupport = createRepositoryFactory(entityManager);
        JadeInvocationHandler handler = new JadeInvocationHandler(new DAOMetaData(getObjectType()),
                entityManager, interpreterFactory, rowMapperFactory, cacheProvider);
        oneRepositoryFactorySupport.setHandler(handler);
		return oneRepositoryFactorySupport;
	}

	/**
	 * Returns a {@link org.springframework.data.repository.core.support.RepositoryFactorySupport}.
	 *
	 * @param entityManager
	 * @return
	 */
	protected OneRepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new OneJpaRepositoryFactory(entityManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */

	public void afterPropertiesSet() {

		Assert.notNull(entityManager, "EntityManager must not be null!");
		super.afterPropertiesSet();
	}
}
