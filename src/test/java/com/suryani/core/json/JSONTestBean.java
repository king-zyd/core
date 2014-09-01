package com.zyd.core.json;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class JSONTestBean {
    @XmlElement(name = "different-field")
    private String field;

    @XmlElement(name = "date")
    private Date date;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<JSONTestBeanItem> items = new ArrayList<JSONTestBeanItem>();

    @XmlElementWrapper(name = "string-items")
    @XmlElement(name = "string-item")
    private List<String> stringItems = new ArrayList<String>();

    @XmlElement(name = "enum-item")
    private JSONTestEnum testEnum = JSONTestEnum.TestEnumOne;

    @XmlElement(name = "map-item")
    private Map<JSONTestEnum, String> testEnumMap = new HashMap<>();

    @XmlElement(name = "classic-enum-item")
    private JSONTestClassicEnum testClassicEnum = JSONTestClassicEnum.TestEnumOne;

    public JSONTestBean() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<JSONTestBeanItem> getItems() {
        return items;
    }

    public List<String> getStringItems() {
        return stringItems;
    }

    public JSONTestEnum getTestEnum() {
        return testEnum;
    }

    public JSONTestClassicEnum getTestClassicEnum() {
        return testClassicEnum;
    }

    public Map<JSONTestEnum, String> getTestEnumMap() {
        return testEnumMap;
    }

    public void setTestEnumMap(Map<JSONTestEnum, String> testEnumMap) {
        this.testEnumMap = testEnumMap;
    }
}
