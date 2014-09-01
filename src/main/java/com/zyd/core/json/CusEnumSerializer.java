package com.zyd.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: gary.zeng
 * Date: 2014/6/23
 */
public class CusEnumSerializer extends StdScalarSerializer<Enum> {

    public CusEnumSerializer() {
        super(Enum.class, false);
    }

    protected CusEnumSerializer(Class<Enum<?>> t) {
        super(t, false);
    }

    protected CusEnumSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        try {
            Method getValueMethod = value.getClass().getDeclaredMethod("getValue");
            jgen.writeString(getValueMethod.invoke(value).toString());
        } catch (NoSuchMethodException e) {
            // no this method, using classic serialize implementation
            jgen.writeString(value.name());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Cannot serialize enum " + value.getClass().getName(), e);
        }
    }
}
