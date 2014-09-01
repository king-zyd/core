package com.zyd.core.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.zyd.core.util.Convert;
import com.zyd.core.util.RuntimeIOException;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author neo
 * @version 1.1 make object mapper singleton according to profiling result
 */
public final class JSONBinder<T> {
    static final ObjectMapper DEFAULT_OBJECT_MAPPER;

    static {
        DEFAULT_OBJECT_MAPPER = createMapper();
    }

    public static <T> JSONBinder<T> binder(Class<T> beanClass) {
        return new JSONBinder<>(beanClass);
    }

    public static ObjectMapper getObjectMapper() {
        return DEFAULT_OBJECT_MAPPER;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleDateFormat dateFormat = new SimpleDateFormat(Convert.DATE_FORMAT_DATETIME);
        mapper.setDateFormat(dateFormat);
        mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);
        mapper.registerModule(new CusEnumModule());
        return mapper;
    }

    private final Class<T> beanClass;
    ObjectMapper objectMapper;

    private JSONBinder(Class<T> beanClass) {
        this.beanClass = beanClass;
        this.objectMapper = DEFAULT_OBJECT_MAPPER;
    }

    public T fromJSON(String json) {
        try {
            return objectMapper.readValue(json, beanClass);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public String toJSON(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public JSONBinder<T> indentOutput() {
        if (DEFAULT_OBJECT_MAPPER.equals(objectMapper)) {
            objectMapper = createMapper();
        }
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return this;
    }
}
