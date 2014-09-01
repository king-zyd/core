package com.zyd.core.platform.concurrent;

/**
 * @author neo
 */
public class TaskTimeoutException extends RuntimeException {
    public TaskTimeoutException(Throwable cause) {
        super(cause);
    }
}
