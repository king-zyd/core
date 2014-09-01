package com.zyd.core.platform.monitor.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chi
 */
@XmlRootElement(name = "monitor-beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class MonitorBeans {
    @XmlElement(name = "name")
    private List<String> names = new ArrayList<>();

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
