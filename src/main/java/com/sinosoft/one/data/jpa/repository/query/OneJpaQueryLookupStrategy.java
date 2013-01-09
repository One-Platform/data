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
package com.sinosoft.one.data.jpa.repository.query;

import com.sinosoft.one.data.jade.annotation.SQL;
import com.sinosoft.one.data.jade.statement.StatementMetaData;
import org.springframework.data.jpa.repository.query.*;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.RepositoryQuery;


import javax.persistence.EntityManager;
import java.lang.reflect.Method;

/**
 * Query lookup strategy to execute finders.
 * 
 * @author Oliver Gierke
 */
public final class OneJpaQueryLookupStrategy {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private OneJpaQueryLookupStrategy() {

	}

	/**
	 * Base class for {@link OneQueryLookupStrategy} implementations that need access to an {@link javax.persistence.EntityManager}.
	 *
	 * @author Oliver Gierke
	 */
	private abstract static class OneAbstractOneQueryLookupStrategy implements OneQueryLookupStrategy {


        private Method method;
        private RepositoryMetadata metadata;
		private final EntityManager em;
		private final QueryExtractor provider;

        protected Method getMethod() {
            return method;
        }
        protected RepositoryMetadata getMetadata() {
            return metadata;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public void setMetadata(RepositoryMetadata metadata) {
            this.metadata = metadata;
        }

        public OneAbstractOneQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

			this.em = em;
			this.provider = extractor;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.springframework.data.repository.query.QueryLookupStrategy#
		 * resolveQuery(java.lang.reflect.Method,
		 * org.springframework.data.repository.core.RepositoryMetadata,
		 * org.springframework.data.repository.core.NamedQueries)
		 */
		public final RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries, SqlQueries sqlQueries) {
            this.method = method;
            this.metadata = metadata;
			return resolveQuery(new JpaQueryMethod(method, metadata, provider), em, namedQueries,sqlQueries);
		}

		protected abstract RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries, SqlQueries sqlQueries);
	}

	/**
	 * {@link org.springframework.data.repository.query.QueryLookupStrategy} to create a query from the method name.
	 *
	 * @author Oliver Gierke
	 */
	private static class OneCreateOneQueryLookupStrategy extends OneAbstractOneQueryLookupStrategy {

		public OneCreateOneQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

			super(em, extractor);
		}


		protected RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries,SqlQueries sqlQueries) {

			try {
				return new PartTreeJpaQuery(method, em);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(String.format("Could not create query metamodel for method %s!",
						method.toString()), e);
			}
		}
	}

	/**
	 * {@link org.springframework.data.repository.query.QueryLookupStrategy} that tries to detect a declared query declared via {@link javax.persistence.Query} annotation followed by
	 * a JPA named query lookup.
	 *
	 * @author Oliver Gierke
	 */
	private static class OneDeclaredOneQueryLookupStrategy extends OneAbstractOneQueryLookupStrategy {

		public OneDeclaredOneQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

			super(em, extractor);
		}


		protected RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries, SqlQueries sqlQueries) {

			RepositoryQuery query = getSpringDataJpaAdapter().simpleJpaQueryFromQueryAnnotation(method, em);

			if (null != query) {
				return query;
			}

			String name = method.getNamedQueryName();
			if (namedQueries.hasQuery(name)) {
				return getSpringDataJpaAdapter().newSimpleJpaQuery(method, em, namedQueries.getQuery(name));
			}

			query = getSpringDataJpaAdapter().namedQueryLookupFrom(method, em);

			if (null != query) {
				return query;
			}

			throw new IllegalStateException(String.format(
					"Did neither find a NamedQuery nor an annotated query for method %s!", method));
		}

	}

	/**
	 * {@link org.springframework.data.repository.query.QueryLookupStrategy} to try to detect a declared query first (
	 * {@link org.springframework.data.jpa.repository.Query}, JPA named query). In case none is found we fall back on
	 * query creation.
	 *
	 * @author Oliver Gierke
	 */
	private static class OneCreateIfNotFoundOneQueryLookupStrategy extends OneAbstractOneQueryLookupStrategy {

		private final OneDeclaredOneQueryLookupStrategy strategy;
		private final OneCreateOneQueryLookupStrategy createStrategy;
        private final OneJadeOneQueryLookupStrategy jadeStrategy;

		public OneCreateIfNotFoundOneQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

			super(em, extractor);
			this.strategy = new OneDeclaredOneQueryLookupStrategy(em, extractor);
			this.createStrategy = new OneCreateOneQueryLookupStrategy(em, extractor);
            this.jadeStrategy = new OneJadeOneQueryLookupStrategy(em, extractor);
		}


		protected RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries, SqlQueries sqlQueries) {

            try {
                this.jadeStrategy.setMethod(getMethod());
                this.jadeStrategy.setMetadata(getMetadata());
                return jadeStrategy.resolveQuery(method, em, namedQueries, sqlQueries);
            } catch (IllegalStateException e) {
                try {
                    return strategy.resolveQuery(method, em, namedQueries, sqlQueries);
                } catch (IllegalStateException e1) {
                    return createStrategy.resolveQuery(method, em, namedQueries, sqlQueries);
                }
            }
		}
	}

    private static class OneJadeOneQueryLookupStrategy extends OneAbstractOneQueryLookupStrategy {

        public OneJadeOneQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {
            super(em, extractor);
        }

        protected RepositoryQuery resolveQuery(JpaQueryMethod method, EntityManager em, NamedQueries namedQueries, SqlQueries sqlQueries) {
            Method m = getMethod();
            String queryName = m.getDeclaringClass().getName()+"."+m.getName();
            SQL sql = m.getAnnotation(SQL.class);
            String sqlValue = (sql==null?sqlQueries.getQuery(queryName):sql.value());
            if(sqlValue!=null && StatementMetaData.CALL_PATTERN.matcher(sqlValue).find()){
                if(m.getReturnType()!=Void.TYPE ){
                    throw new IllegalArgumentException(String.format(
                            "It requires a 'Void type' for the procedure method: %s!", method));
                }
            }
            RepositoryQuery query = OneJadeRepositoryQuery.fromSQLAnnotation(new OneJadeQueryMethod(getMethod(), getMetadata()), em, sqlQueries);

            if (null != query) {
                return query;
            }
            throw new IllegalStateException(String.format(
                    "Did not find an annotated SQL for method %s!", method));

        }
    }
	/**
	 * Creates a {@link org.springframework.data.repository.query.QueryLookupStrategy} for the given {@link javax.persistence.EntityManager} and {@link org.springframework.data.repository.query.QueryLookupStrategy.Key}.
	 * 
	 * @param em
	 * @param key
	 * @return
	 */
	public static OneQueryLookupStrategy create(EntityManager em, OneQueryLookupStrategy.Key key, QueryExtractor extractor) {

		if (key == null) {
			return new OneCreateIfNotFoundOneQueryLookupStrategy(em, extractor);
		}

		switch (key) {
		case CREATE:
			return new OneCreateOneQueryLookupStrategy(em, extractor);
		case USE_DECLARED_QUERY:
			return new OneDeclaredOneQueryLookupStrategy(em, extractor);
		case CREATE_IF_NOT_FOUND:
			return new OneCreateIfNotFoundOneQueryLookupStrategy(em, extractor);
		default:
			throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
		}
	}

    public static SpringDataJpaQueryAdapter getSpringDataJpaAdapter() {
        return SpringDataJpaQueryAdapter.getInstance();
    }
}