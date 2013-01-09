package com.sinosoft.one.data.jpa.repository.query;

import java.util.Enumeration;

/**
 * User: Morgan
 * Date: 12-8-15
 * Time: 下午12:37
 */
public interface SqlQueries {
	/**
	 * Returns whether the map contains a named query for the given name. If this method returns {@literal true} you can
	 * expect {@link #getQuery(String)} to return a non-{@literal null} query for the very same name.
	 *
	 * @param queryName
	 * @return
	 */
	boolean hasQuery(String queryName);

	/**
	 * Returns the named query with the given name or {@literal null} if none exists.
	 *
	 * @param queryName
	 * @return
	 */
	String getQuery(String queryName);

	Enumeration<String> getPropertyNames() ;
}
