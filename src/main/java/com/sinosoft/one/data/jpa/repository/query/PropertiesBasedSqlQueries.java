package com.sinosoft.one.data.jpa.repository.query;

import org.springframework.util.Assert;

import java.util.Enumeration;
import java.util.Properties;

/**
 * User: Morgan
 * Date: 12-8-15
 * Time: 下午12:35
 */
public class PropertiesBasedSqlQueries implements SqlQueries {
	public static final SqlQueries EMPTY = new PropertiesBasedSqlQueries(new Properties());

	private final Properties properties;

	/**
	 * Creates a new {@link PropertiesBasedSqlQueries} for the given {@link Properties} instance.
	 *
	 * @param properties
	 */
	public PropertiesBasedSqlQueries(Properties properties) {
		Assert.notNull(properties);
		this.properties = properties;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.repository.core.NamedQueries#hasNamedQuery(java.lang.String)
	 */
	public boolean hasQuery(String queryName) {
		return properties.containsKey(queryName);
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.repository.core.NamedQueries#getNamedQuery(java.lang.String)
	 */
	public String getQuery(String queryName) {
		return properties.getProperty(queryName);
	}

	public Enumeration<String> getPropertyNames() {
		return (Enumeration<String>) this.properties.propertyNames();
	}
}
