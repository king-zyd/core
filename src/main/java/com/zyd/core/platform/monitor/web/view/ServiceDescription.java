package com.zyd.core.platform.monitor.web.view;

import com.zyd.core.util.AssertUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chi
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceDescription {
    @XmlElement(name = "path")
    private String path;
    @XmlElementWrapper(name = "path-variables")
    @XmlElement(name = "path-variable")
    private List<ObjectDescription> pathVariables;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "method")
    private String method;
    @XmlElement(name = "request")
    private ObjectDescription request;
    @XmlElement(name = "response")
    private ObjectDescription response;

    public void addPathVariable(ObjectDescription pathVariable) {
        if (pathVariables == null) {
            pathVariables = new ArrayList<>();
        }
        pathVariables.add(pathVariable);
    }

    public void setRequest(ObjectDescription request) {
        AssertUtils.assertNull(this.request, "one service method can only contain one @RequestBody");
        this.request = request;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ObjectDescription> getPathVariables() {
        return pathVariables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ObjectDescription getRequest() {
        return request;
    }

    public ObjectDescription getResponse() {
        return response;
    }

    public void setResponse(ObjectDescription response) {
        this.response = response;
    }
}
