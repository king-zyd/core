package com.zyd.core.platform.management.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * @author neo
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class CacheGroupView {
    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "revision")
    private int revision;

    @XmlElement(name = "last-refreshed")
    private Date lastRefreshed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public Date getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(Date lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }
}
