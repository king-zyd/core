package com.zyd.core.http;

/**
 * @author neo
 */
public interface ParameterBuilder extends RequestBuilder {
    ParameterBuilder addParameter(String key, String value);

    ParameterBuilder setParameter(String key, String value);
}
