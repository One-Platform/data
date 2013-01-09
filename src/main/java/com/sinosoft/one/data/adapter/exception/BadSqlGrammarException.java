package com.sinosoft.one.data.adapter.exception;

import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.sql.*;

public class BadSqlGrammarException extends InvalidDataAccessResourceUsageException {

	private String sql;


	/**
	 * Constructor for BadSqlGrammarException.
	 * @param task name of current task
	 * @param sql the offending SQL statement
	 * @param ex the root cause
	 */
	public BadSqlGrammarException(String task, String sql, SQLException ex) {
		super(task + "; bad SQL grammar [" + sql + "]", ex);
		this.sql = sql;
	}


	/**
	 * Return the wrapped SQLException.
	 */
	public java.sql.SQLException getSQLException() {
		return (java.sql.SQLException) getCause();
	}

	/**
	 * Return the SQL that caused the problem.
	 */
	public String getSql() {
		return this.sql;
	}

}

