package com.zyd.core.platform.exception;

/**
 * @author neo
 */
public class BusinessProcessException extends RuntimeException {
    public BusinessProcessException(String message) {
        super(message);
    }

    public BusinessProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}