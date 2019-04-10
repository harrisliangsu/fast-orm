package com.shark.feifei.Exception;

/**
 * Jdbc exception
 * @Author: Shark Chili
 * @Date: 2018/11/1 0001
 */
public class JdbcException extends FeifeiRuntimeException{
	public JdbcException() {
		super();
	}

	public JdbcException(String message, Object... args) {
		super(message, args);
	}

	public JdbcException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public JdbcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
