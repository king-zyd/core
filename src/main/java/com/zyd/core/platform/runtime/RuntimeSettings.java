package com.zyd.core.platform.runtime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author neo
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class RuntimeSettings {
    private static final RuntimeSettings INSTANCE = new RuntimeSettings();

    public static RuntimeSettings get() {
        return INSTANCE;
    }

    // make it easy for internal REST service, default to DEV to show exception trace
    @XmlElement(name = "environment")
    private RuntimeEnvironment environment = RuntimeEnvironment.DEV;
    @XmlElement(name = "version")
    private String version = "current";

    public RuntimeEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(RuntimeEnvironment environment) {
        this.environment = environment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
