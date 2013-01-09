package com.sinosoft.one.data.jpa.repository.query;

import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * User: Morgan
 * Date: 12-8-16
 * Time: 上午9:27
 */
public interface OneQueryLookupStrategy {


	public static enum Key {

		CREATE, USE_DECLARED_QUERY, CREATE_IF_NOT_FOUND;

		/**
		 * Returns a strategy key from the given XML value.
		 *
		 * @param xml
		 * @return a strategy key from the given XML value
		 */
		public static Key create(String xml) {

			if (!StringUtils.hasText(xml)) {
				return null;
			}

			return valueOf(xml.toUpperCase(Locale.US).replace("-", "_"));
		}
	}

	/**
	 * Resolves a {@link org.springframework.data.repository.query.RepositoryQuery} from the given {@link org.springframework.data.repository.query.QueryMethod} that can be executed afterwards.
	 *
	 * @param method
	 * @param metadata
	 * @param namedQueries
	 * @return
	 */
	RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries, SqlQueries sqlQueries);

}
