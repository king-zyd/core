package com.zyd.core.collection.converter;

import com.zyd.core.json.JSONBinder;

/**
 * @author neo
 */
public class JSONConverter {
    public <T> T fromString(Class<T> targetClass, String value) {
        return JSONBinder.binder(targetClass).fromJSON(value);
    }

    @SuppressWarnings("unchecked")
    public <T> String toString(T value) {
        return JSONBinder.binder((Class<T>) value.getClass()).toJSON(value);
    }
}
