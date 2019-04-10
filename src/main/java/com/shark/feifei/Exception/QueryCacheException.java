package com.shark.feifei.Exception;

/**
 * Query cache exception
 * @Author: Shark Chili
 * @Date: 2018/11/13 0013
 */
public class QueryCacheException extends FeifeiRuntimeException{
	public QueryCacheException() {
		super();
	}

	public QueryCacheException(String message, Object... args) {
		super(message, args);
	}

	public QueryCacheException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public QueryCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
