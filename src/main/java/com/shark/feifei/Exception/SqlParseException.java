package com.shark.feifei.Exception;

/**
 * Sql parse exception
 * @Author: Shark Chili
 * @Date: 2018/10/31 0031
 */
public class SqlParseException extends FeifeiRuntimeException{
	public SqlParseException() {
		super();
	}

	public SqlParseException(String message, Object... args) {
		super(message, args);
	}

	public SqlParseException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public SqlParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
