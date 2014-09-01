package com.zyd.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author neo
 */
public class StringUtilsTest {
    @Test
    public void isGUID() {
        assertEquals(true, StringUtils.isGUID("0924ca91-3ac0-4ab9-875e-0c76ebe91f01"));
        assertEquals(true, StringUtils.isGUID("0924CA91-3AC0-4AB9-875E-0C76EBE91F01"));
        assertEquals(false, StringUtils.isGUID("0924ca91-3ac0-4ab9-875e-0C76EBE91"));
        assertEquals(false, StringUtils.isGUID("0924ca91-3ac0-4ab9-875e-0c76ebe91f011"));
        assertEquals(false, StringUtils.isGUID("0924ca91-3ac0-4ab9-875e-0c76ebe91f012"));
        assertEquals(false, StringUtils.isGUID(""));
        assertEquals(false, StringUtils.isGUID(null));
    }

    @Test
    public void nullStringsEqual() {
        assertEquals(true, StringUtils.equals(null, null));
    }

    @Test
    public void nullStringNotEqualsToEmpty() {
        assertEquals(false, StringUtils.equals(null, ""));
        assertEquals(false, StringUtils.equals("", null));
    }

    @Test
    public void emptyStringsEqual() {
        assertEquals(true, StringUtils.equals("", ""));
    }

    @Test
    public void nullStringIsLessThanEmptyString() {
        assertEquals(-1, StringUtils.compare(null, ""));
    }

    @Test
    public void nullStringEqualsToNull() {
        assertEquals(0, StringUtils.compare(null, null));
    }

    @Test
    public void compareRegularStrings() {
        assertEquals(1, StringUtils.compare("b", "a"));
    }

    @Test
    public void truncateNull() {
        String value = StringUtils.truncate(null, 10);

        assertNull(value);
    }

    @Test
    public void truncateTextShorterThanMaxLength() {
        String value = StringUtils.truncate("value", 10);

        assertEquals("value", value);
    }

    @Test
    public void truncateTextLongerThanMaxLength() {
        String value = StringUtils.truncate("123456789012345", 10);

        assertEquals("1234567890", value);
    }
}
