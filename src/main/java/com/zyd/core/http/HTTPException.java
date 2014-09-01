package com.zyd.core.http;

/**
 * @author neo
 */
public class HTTPException extends RuntimeException {
    public HTTPException(String message) {
        super(message);
    }

    public HTTPException(Throwable cause) {
        super(cause);
    }
}
