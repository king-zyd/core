package com.zyd.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: gary.zeng
 * Date: 2014/6/23
 */
public class CusEnumDeserializer extends StdScalarDeserializer<Enum<?>> {

    public CusEnumDeserializer() {
        this(Enum.class);
    }

    protected CusEnumDeserializer(Class<?> vc) {
        super(vc);
    }

    protected CusEnumDeserializer(JavaType valueType) {
        super(valueType);
    }

    @Override
    public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = jp.getText();
        try {
            Method parseMethod = getValueClass().getDeclaredMethod("parse", String.class);
            Enum<?> value = (Enum<?>) parseMethod.invoke(null, text);
            if (value != null) {
                return value;
            }
            return findEnumByName(text);
        } catch (NoSuchMethodException e) {
            return findEnumByName(text);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Cannot deserialize enum " + getValueClass().getName() + " from " + text, e);
        }
    }

    private Enum<?> findEnumByName(String name) {
        Enum<?>[] enumConstants = (Enum<?>[]) getValueClass().getEnumConstants();
        for (Enum<?> enumEle : enumConstants) {
            if (enumEle.name().equals(name)) {
                return enumEle;
            }
        }
        return null;
    }
}
