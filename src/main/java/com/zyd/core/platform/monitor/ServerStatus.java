package com.zyd.core.platform.monitor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author neo
 */
@XmlRootElement(name = "server_status")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerStatus {
    @XmlElement(name = "status")
    private ServiceStatus status = ServiceStatus.UP;

    public ServiceStatus status() {
        return status;
    }

    public void disable() {
        status = ServiceStatus.DISABLED;
    }

    public void enable() {
        status = ServiceStatus.UP;
    }
}
