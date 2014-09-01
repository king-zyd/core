package com.zyd.core.collection;

import com.zyd.core.collection.converter.DateConverter;
import com.zyd.core.collection.converter.JSONConverter;
import com.zyd.core.collection.converter.NumberConverter;
import com.zyd.core.util.StringUtils;

import java.util.Date;

/**
 * @author neo
 */
public class TypeConverter {
    static final String ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_PRIMITIVE = "targetClass cannot be primitive, use wrapper class instead, e.g. Integer.class for int.class";

    final DateConverter dateConverter = new DateConverter();
    final NumberConverter numberConverter = new NumberConverter();
    final JSONConverter jsonConverter = new JSONConverter();

    @SuppressWarnings("unchecked")
    public <T> T fromString(String textValue, Class<T> targetClass) {
        if (targetClass.isPrimitive())
            throw new IllegalArgumentException(ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_PRIMITIVE);

        if (String.class.equals(targetClass)) return (T) textValue;
        if (!StringUtils.hasText(textValue)) return null;

        if (Boolean.class.equals(targetClass)) return (T) Boolean.valueOf(textValue);

        if (Number.class.isAssignableFrom(targetClass))
            return (T) numberConverter.convertToNumber(textValue, (Class<? extends Number>) targetClass);

        if (Character.class.equals(targetClass)) return (T) Character.valueOf(textValue.charAt(0));

        if (Enum.class.isAssignableFrom(targetClass)) return (T) Enum.valueOf((Class<Enum>) targetClass, textValue);
        if (Date.class.equals(targetClass)) return (T) dateConverter.fromString(textValue);

        return jsonConverter.fromString(targetClass, textValue);
    }

    @SuppressWarnings("unchecked")
    public <T> String toString(T value) {
        if (value == null) return "";
        if (value instanceof String) return (String) value;
        if (value instanceof Boolean) return String.valueOf(value);
        if (value instanceof Number) return String.valueOf(value);
        if (value instanceof Enum) return ((Enum) value).name();
        if (value instanceof Character) return String.valueOf(value);
        if (value instanceof Date) return dateConverter.toString((Date) value);

        return jsonConverter.toString(value);
    }
}
