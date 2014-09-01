package com.zyd.core.platform.monitor.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chi
 */
@XmlRootElement(name = "service-descriptions")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceDescriptions {
    @XmlElementWrapper(name = "descriptions")
    @XmlElement(name = "description")
    private final List<ServiceDescription> descriptions = new ArrayList<ServiceDescription>();

    public List<ServiceDescription> getDescriptions() {
        return descriptions;
    }
}
