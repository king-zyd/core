package com.zyd.core.platform.exception;

/**
 * @author neo
 */
public class InternalServiceException extends RuntimeException {
    public InternalServiceException(String message) {
        super(message);
    }

    public InternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
