package com.zyd.core.platform.monitor.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Chi
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectDescription {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "json-schema")
    private String jsonSchema;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonSchema() {
        return jsonSchema;
    }

    public void setJsonSchema(String schema) {
        this.jsonSchema = schema;
    }
}