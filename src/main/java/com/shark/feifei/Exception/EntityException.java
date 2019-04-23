package com.shark.feifei.Exception;

public class EntityException extends FeifeiRuntimeException {
    public EntityException() {
        super();
    }

    public EntityException(String message, Object... args) {
        super(message, args);
    }

    public EntityException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }

    public EntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
