package com.zyd.core.collection;

/**
 * @author neo
 */
public class TypeConversionException extends RuntimeException {
    public TypeConversionException(String message) {
        super(message);
    }

    public TypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
