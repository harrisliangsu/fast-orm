package com.shark.feifei.Exception;

/**
 * Sql exception
 * @Author: Shark Chili
 * @Date: 2018/11/12 0012
 */
public class SqlException extends FeifeiRuntimeException{
	public SqlException() {
		super();
	}

	public SqlException(String message, Object... args) {
		super(message, args);
	}

	public SqlException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public SqlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
