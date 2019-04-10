package com.shark.feifei.Exception;

/**
 * Connection exception
 * @Author: Shark Chili
 * @Date: 2018/11/15 0015
 */
public class ConnectionException extends FeifeiRuntimeException{
	public ConnectionException() {
		super();
	}

	public ConnectionException(String message, Object... args) {
		super(message, args);
	}

	public ConnectionException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public ConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
