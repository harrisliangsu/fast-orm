package com.shark.feifei.Exception;

/**
 * Query exception
 * @Author: Shark Chili
 * @Date: 2018/10/31 0031
 */
public class QueryException extends FeifeiRuntimeException{
	public QueryException() {
		super();
	}

	public QueryException(String message, Object... args) {
		super(message, args);
	}

	public QueryException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public QueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
