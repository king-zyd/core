package com.zyd.core.http;

/**
 * @author neo
 */
public interface HeaderBuilder extends ParameterBuilder {
    HeaderBuilder addHeader(String key, String value);

    HeaderBuilder accept(String accept);
}
