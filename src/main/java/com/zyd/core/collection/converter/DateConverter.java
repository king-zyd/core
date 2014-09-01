package com.zyd.core.collection.converter;


import com.zyd.core.collection.TypeConversionException;
import com.zyd.core.util.Convert;

import java.util.Date;

/**
 * @author neo
 */
public class DateConverter {
    public Date fromString(String property) {
        return parseDateTime(property);
    }

    public String toString(Date value) {
        return Convert.toString(value, Convert.DATE_FORMAT_DATETIME);
    }

    Date parseDateTime(String textValue) {
        Date date = Convert.toDateTime(textValue, null);
        if (date == null)
            date = Convert.toDate(textValue, (Date) null);
        if (date == null)
            throw new TypeConversionException("can not convert to date, text=" + textValue);
        return date;
    }
}
