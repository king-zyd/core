package com.zyd.core.collection;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class TypeConverterObjectValueTest {
    TypeConverter converter;

    @Before
    public void createConverter() {
        converter = new TypeConverter();
    }

    public static class TestBean {
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    @Test
    public void convertObject() {
        TestBean bean = new TestBean();
        bean.setField("value");
        String text = converter.toString(bean);
        assertThat(text, containsString("\"field\":\"value\""));

        TestBean convertedBean = converter.fromString(text, TestBean.class);
        assertEquals(bean.getField(), convertedBean.getField());
    }
}
