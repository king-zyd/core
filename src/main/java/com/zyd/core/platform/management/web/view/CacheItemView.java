package com.zyd.core.platform.management.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author neo
 */
@XmlRootElement(name = "cache-item")
@XmlAccessorType(XmlAccessType.FIELD)
public class CacheItemView {
    @XmlElement(name = "group")
    private String group;
    @XmlElement(name = "key")
    private String key;
    @XmlElement(name = "value")
    private Object value;
    @XmlElement(name = "created-time")
    private Date createdTime;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
