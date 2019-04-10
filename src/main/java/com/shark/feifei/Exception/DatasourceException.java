package com.shark.feifei.Exception;

/**
 * Datasource exception
 * @Author: Shark Chili
 * @Date: 2018/11/19 0019
 */
public class DatasourceException extends FeifeiRuntimeException{
	public DatasourceException() {
		super();
	}

	public DatasourceException(String message, Object... args) {
		super(message, args);
	}

	public DatasourceException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public DatasourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
