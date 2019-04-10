package com.shark.feifei.Exception;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/3 0003
 */
public class ConfigException extends FeifeiRuntimeException{

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Object... args) {
		super(message, args);
	}

	public ConfigException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
