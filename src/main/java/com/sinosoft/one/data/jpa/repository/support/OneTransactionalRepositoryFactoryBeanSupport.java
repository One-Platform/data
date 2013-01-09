/*
 * Copyright 2008-2010 the original author or authors.
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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.springframework.data.repository.util.TxUtils;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Extension of {@link OneRepositoryFactoryBeanSupport} to add transactional capabilities to the repository proxy. Will
 * register a {@link org.springframework.data.repository.core.support.TransactionalRepositoryProxyPostProcessor} that in turn adds a {@link org.springframework.transaction.interceptor.TransactionInterceptor} to
 * the repository proxy to be created.
 *
 * @author Oliver Gierke
 */
public abstract class OneTransactionalRepositoryFactoryBeanSupport<T extends Repository<S, ID>, S, ID extends Serializable>
		extends OneRepositoryFactoryBeanSupport<T, S, ID> implements BeanFactoryAware {

	private String transactionManagerName = TxUtils.DEFAULT_TRANSACTION_MANAGER;
	private RepositoryProxyPostProcessor txPostProcessor;

	/**
	 * Setter to configure which transaction manager to be used. We have to use the bean name explicitly as otherwise the
	 * qualifier of the {@link org.springframework.transaction.annotation.Transactional} annotation is used. By explicitly
	 * defining the transaction manager bean name we favour let this one be the default one chosen.
	 *
	 * @param transactionManager
	 */
	public void setTransactionManager(String transactionManager) {

		this.transactionManagerName = transactionManager == null ? TxUtils.DEFAULT_TRANSACTION_MANAGER : transactionManager;
	}

	/**
	 * Delegates {@link OneRepositoryFactorySupport} creation to {@link #doCreateRepositoryFactory()} and applies the
	 * {@link org.springframework.data.repository.core.support.TransactionalRepositoryProxyPostProcessor} to the created instance.
	 *
	 * @see OneRepositoryFactorySupport #createRepositoryFactory()
	 */

	protected final OneRepositoryFactorySupport createRepositoryFactory() {

		OneRepositoryFactorySupport factory = doCreateRepositoryFactory();
		factory.addRepositoryProxyPostProcessor(txPostProcessor);
		return factory;
	}

	/**
	 * Creates the actual {@link OneRepositoryFactorySupport} instance.
	 * 
	 * @return
	 */
	protected abstract OneRepositoryFactorySupport doCreateRepositoryFactory();

	/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org
			 * .springframework.beans.factory.BeanFactory)
			 */
	public void setBeanFactory(BeanFactory beanFactory) {

		Assert.isInstanceOf(ListableBeanFactory.class, beanFactory);

		this.txPostProcessor = new OneTransactionalRepositoryProxyPostProcessor((ListableBeanFactory) beanFactory,
                transactionManagerName, sqlQueries);
	}
}
