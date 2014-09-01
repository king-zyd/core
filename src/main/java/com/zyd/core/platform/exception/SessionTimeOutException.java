package com.zyd.core.platform.exception;

/**
 * @author neo
 */
@Warning
public class SessionTimeOutException extends RuntimeException {
    public SessionTimeOutException(String message) {
        super(message);
    }

    public SessionTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }
}
