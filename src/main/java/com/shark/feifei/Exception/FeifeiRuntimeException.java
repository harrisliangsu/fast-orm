package com.shark.feifei.Exception;


import com.shark.util.util.StringUtil;

/**
 * The abstract feifei implement class of RuntimeException
 * @Author: Shark Chili
 * @Date: 2018/10/31 0031
 */
abstract class FeifeiRuntimeException extends RuntimeException{
	FeifeiRuntimeException() {
		super();
	}

	FeifeiRuntimeException(String message, Object... args) {
		super(StringUtil.format(message,args));
	}

	FeifeiRuntimeException(String message, Throwable cause, Object... args) {
		super(StringUtil.format(message,args), cause);
	}

	FeifeiRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(StringUtil.format(message,args), cause, enableSuppression, writableStackTrace);
	}
}
