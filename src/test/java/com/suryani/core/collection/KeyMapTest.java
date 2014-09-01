package com.zyd.core.collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class KeyMapTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    KeyMap map;

    @Before
    public void createContext() {
        map = new KeyMap();
    }

    @Test
    public void getObjectWithUnmatchedClass() {
        exception.expect(TypeConversionException.class);
        exception.expectMessage("targetClass=" + Integer.class.getName());
        exception.expectMessage("expectedClass=" + String.class.getName());

        map.context.put("number", 1);
        map.get("number", String.class);
    }

    @Test
    public void putNull() {
        Key<String> key = Key.stringKey("key");
        map.put(key, null);

        String value = map.get(key);

        assertNull(value);
    }

    @Test
    public void putMap() {
        Key<String> key = Key.stringKey("key");

        HashMap<String, String> properties = new HashMap<>();
        properties.put("key", "value");
        map.putAll(properties);

        String value = map.get(key);

        assertEquals("value", value);
    }

    @Test
    public void serialize() {
        map.put(Key.stringKey("key1"), "value");
        map.put(Key.stringKey("key2"), null);
        String text = map.serialize();
        assertThat(text, containsString("key1=value"));
        assertThat(text, containsString("key2="));
    }

    @Test
    public void deserialize() {
        String text = "key1=value\nkey2=";
        map.deserialize(text);

        assertEquals("value", map.get(Key.stringKey("key1")));
        assertNull(map.get(Key.stringKey("key2")));
    }

    @XmlType
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestBean {
        @XmlElement(name = "field")
        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    @Test
    public void saveObject() {
        Key<TestBean> key = new Key<>("bean", TestBean.class);

        TestBean bean = new TestBean();
        bean.setField("value=some");
        map.put(key, bean);

        String persistedText = map.serialize();
        map.clear();
        map.deserialize(persistedText);

        TestBean loadedBean = map.get(key);

        Assert.assertEquals(bean.getField(), loadedBean.getField());
    }
}
