package org.springframework.data.jpa.repository.query;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.RepositoryQuery;

import javax.persistence.EntityManager;

/**
 * spring data jpa adapter
 * User: carvin
 * Date: 12-8-13
 * Time: 下午2:56
 * To change this template use File | Settings | File Templates.
 */
public final class SpringDataJpaQueryAdapter {
    private SpringDataJpaQueryAdapter() {

    }
    private static SpringDataJpaQueryAdapter instance = new SpringDataJpaQueryAdapter();

    public static SpringDataJpaQueryAdapter getInstance() {
        return instance;
    }

    public RepositoryQuery simpleJpaQueryFromQueryAnnotation(JpaQueryMethod method, EntityManager em) {
        return SimpleJpaQuery.fromQueryAnnotation(method, em);
    }

    public SimpleJpaQuery newSimpleJpaQuery(JpaQueryMethod method, EntityManager em, String queryString) {
        return new SimpleJpaQuery(method, em, queryString);
    }

    public RepositoryQuery namedQueryLookupFrom(JpaQueryMethod method, EntityManager em) {
        return NamedQuery.lookupFrom(method, em);
    }


}
