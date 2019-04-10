package com.shark.feifei.Exception;

/**
 * Cache exception
 * @Author: Shark Chili
 * @Date: 2018/11/15 0015
 */
public class CacheException extends FeifeiRuntimeException{
	public CacheException() {
		super();
	}

	public CacheException(String message, Object... args) {
		super(message, args);
	}

	public CacheException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public CacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
