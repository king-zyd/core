package com.zyd.core.json;

import com.zyd.core.util.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class JSONBinderTest {
    @Test
    public void supportJAXBAnnotations() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = new JSONTestBean();
        bean.setField("value");

        String json = binder.toJSON(bean);

        assertThat(json, containsString("\"different-field\":\"value\""));
    }

    @Test
    public void toJSONWithEnumMap() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = new JSONTestBean();
        Map<JSONTestEnum, String> testEnumMap = new HashMap<>();
        testEnumMap.put(JSONTestEnum.TestEnumOne, "Test Map One");
        bean.setTestEnumMap(testEnumMap);
        String json = binder.toJSON(bean);
        assertThat(json, containsString("\"map-item\":{\"TestEnumOne\":\"Test Map One\"}"));
    }

    @Test
    public void fromJSONWithEnumMap() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        String json = "{\"map-item\":{\"TestEnumOne\":\"Test Map One\"}}";
        JSONTestBean bean = binder.fromJSON(json);
        Assert.assertEquals(true, bean.getTestEnumMap().containsKey(JSONTestEnum.TestEnumOne));
    }

    @Test
    public void toJSONCustomEnumSer() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = new JSONTestBean();
        String json = binder.toJSON(bean);
        assertThat(json, containsString("\"enum-item\":\"Test Enum One\""));
    }

    @Test
    public void toJSONClassicEnum() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = new JSONTestBean();
        String json = binder.toJSON(bean);
        assertThat(json, containsString("\"classic-enum-item\":\"TestEnumOne\""));
    }

    @Test
    public void fromJSONCustomEnumDeser() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        String json = "{\"enum-item\":\"Test Enum One\"}";
        JSONTestBean bean = binder.fromJSON(json);
        Assert.assertEquals(JSONTestEnum.TestEnumOne, bean.getTestEnum());
    }

    @Test
    public void fromJSONClassicEnum() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        String json = "{\"classic-enum-item\":\"TestEnumOne\"}";
        JSONTestBean bean = binder.fromJSON(json);
        Assert.assertEquals(JSONTestClassicEnum.TestEnumOne, bean.getTestClassicEnum());
    }

    @Test
    public void fromJSON() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = binder.fromJSON("{\"different-field\":\"value\"}");

        Assert.assertEquals("value", bean.getField());
    }

    @Test
    public void fromJSONWithUnknownFields() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = binder.fromJSON("{\"different-field\":\"value\", \"non-existed-field\":\"someValue\"}");

        Assert.assertEquals("value", bean.getField());
    }

    @Test
    public void serializeDate() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = new JSONTestBean();
        Date date = DateUtils.date(2012, Calendar.APRIL, 18, 11, 30, 0);
        bean.setDate(date);

        String json = binder.toJSON(bean);

        assertThat(json, containsString("\"date\":\"2012-04-18T11:30:00\""));

        JSONTestBean result = binder.fromJSON(json);

        Assert.assertEquals(date, result.getDate());
    }

    @Test
    public void serializeListWithWrapper() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = new JSONTestBean();
        JSONTestBeanItem item = new JSONTestBeanItem();
        item.setField("value");
        bean.getItems().add(item);

        String json = binder.toJSON(bean);
        assertThat(json, containsString("\"items\":[{\"field\":\"value\"}]"));
    }

    @Test
    public void serializeStringListWithWrapper() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        JSONTestBean bean = new JSONTestBean();
        bean.getStringItems().add("value1");
        bean.getStringItems().add("value2");

        String json = binder.toJSON(bean);
        assertThat(json, containsString("\"string-items\":[\"value1\",\"value2\"]"));
    }

    @Test
    public void configureShouldCreateDifferentMapper() {
        JSONBinder<JSONTestBean> binder = JSONBinder.binder(JSONTestBean.class);
        binder.indentOutput();

        Assert.assertNotSame(JSONBinder.DEFAULT_OBJECT_MAPPER, binder.objectMapper);
    }
}
