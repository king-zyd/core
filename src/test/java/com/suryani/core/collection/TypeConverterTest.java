package com.zyd.core.collection;

import com.zyd.core.util.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author neo
 */
public class TypeConverterTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    TypeConverter converter;

    @Before
    public void createConverter() {
        converter = new TypeConverter();
    }

    @Test
    public void fromStringShouldNotAcceptPrimitiveTypeAsTargetClass() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(TypeConverter.ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_PRIMITIVE);

        converter.fromString("1", int.class);
    }

    @Test
    public void convertEmptyToObject() {
        Object value = converter.fromString("", Object.class);

        assertNull(value);
    }

    public static enum MockEnum {
        VALUE1, VALUE2
    }

    @Test
    public void convertStringToEnum() {
        MockEnum value = converter.fromString(MockEnum.VALUE1.name(), MockEnum.class);
        assertEquals(MockEnum.VALUE1, value);
    }

    @Test
    public void convertEnumToString() {
        String property = converter.toString(MockEnum.VALUE2);
        assertEquals(MockEnum.VALUE2.name(), property);
    }

    @Test
    public void convertStringToString() {
        String stringValue = "stringValue";
        assertEquals(stringValue, converter.fromString(stringValue, String.class));
        assertEquals("stringValue", converter.toString("stringValue"));
    }

    @Test
    public void convertNullToInteger() {
        Integer intValue = converter.fromString(null, Integer.class);
        assertNull(intValue);
    }

    @Test
    public void convertStringToInteger() {
        Integer intValue = converter.fromString("100", Integer.class);
        assertEquals((Integer) 100, intValue);
    }

    @Test
    public void convertNullToBoolean() {
        Boolean boolValue = converter.fromString(null, Boolean.class);
        assertNull(boolValue);
    }

    @Test
    public void convertStringToDate() {
        Date date = converter.fromString("2009-08-15T00:00:00", Date.class);
        assertEquals(DateUtils.date(2009, Calendar.AUGUST, 15), date);
    }

    @Test
    public void convertDateToString() {
        Date date = DateUtils.date(2009, Calendar.AUGUST, 15);
        assertEquals("2009-08-15T00:00:00", converter.toString(date));
    }

    @Test
    public void convertBoolToString() {
        assertEquals("true", converter.toString(true));
        assertEquals("false", converter.toString(false));
    }

    @Test
    public void convertBooleanToString() {
        assertEquals("true", converter.toString(Boolean.TRUE));
        assertEquals("false", converter.toString(Boolean.FALSE));
    }

    @Test
    public void convertCharToString() {
        assertEquals("a", converter.toString('a'));
        assertEquals("1", converter.toString('1'));
    }

    @Test
    public void convertTimestampToString() {
        Date date = DateUtils.date(2010, Calendar.MAY, 4, 11, 30, 0);

        Timestamp timestamp = new Timestamp(date.getTime());

        assertEquals("2010-05-04T11:30:00", converter.toString(timestamp));
    }
}
