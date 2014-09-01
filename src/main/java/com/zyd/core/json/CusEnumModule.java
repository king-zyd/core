package com.zyd.core.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * User: gary.zeng
 * Date: 2014/6/23
 */
public class CusEnumModule extends SimpleModule {

    public CusEnumModule() {
        super("suryani-core-enum", new Version(1, 0, 0, "", "com.zyd.core.json", "suryani-core-enum"));
        addSerializer(Enum.class, new CusEnumSerializer());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        Deserializers.Base deser = new Deserializers.Base() {
            @SuppressWarnings("unchecked")
            @Override
            public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc)
                throws JsonMappingException {
                return new CusEnumDeserializer((Class<Enum<?>>) type);
            }
        };
        context.addDeserializers(deser);
    }
}
