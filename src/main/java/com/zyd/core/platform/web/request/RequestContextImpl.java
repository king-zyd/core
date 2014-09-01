package com.zyd.core.platform.web.request;

import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.util.ReadOnly;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;

/**
 * @author neo
 */
public class RequestContextImpl implements RequestContext {
    private final ReadOnly<HttpServletRequest> request = new ReadOnly<>();
    private final ReadOnly<String> clientId = new ReadOnly<>();
    private final ReadOnly<String> requestId = new ReadOnly<>();
    private final ReadOnly<Date> requestDate = new ReadOnly<>();
    private final ReadOnly<String> action = new ReadOnly<>(); // the action method, controllerClass-method

    private DeploymentSettings deploymentSettings;
    private RequestURLHelper requestURLHelper;

    @Override
    public String getClientRequestedFullURLWithQueryString() {
        return requestURLHelper.getClientRequestedFullURLWithQueryString();
    }

    @Override
    public String getClientRequestedRelativeURLWithQueryString() {
        return requestURLHelper.getClientRequestedRelativeURLWithQueryString();
    }

    @Override
    public String getClientRequestedRelativeURL() {
        return requestURLHelper.getClientRequestedRelativeURL();
    }

    @Override
    public String getClientRequestServerName() {
        return request.value().getServerName();
    }

    @Override
    public boolean isSecure() {
        return request.value().isSecure();
    }

    @Override
    public RemoteAddress getRemoteAddress() {
        return RemoteAddress.create(request.value());
    }

    @Override
    public String getClientId() {
        return clientId.value();
    }

    @Override
    public String getRequestId() {
        return requestId.value();
    }

    @Override
    public Date getRequestDate() {
        return requestDate.value();
    }

    @Override
    public String getAction() {
        return action.value();
    }
    
    @Override
    public String getRequestHeader(String name) {
        return request.value().getHeader(name);
    }

    public void setClientId(String clientId) {
        this.clientId.set(clientId);
    }

    public void setRequestId(String requestId) {
        this.requestId.set(requestId);
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate.set(requestDate);
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public void setHTTPRequest(HttpServletRequest request) {
        this.request.set(request);
        requestURLHelper = new RequestURLHelper(request, deploymentSettings);
    }

    @Inject
    public void setDeploymentSettings(DeploymentSettings deploymentSettings) {
        this.deploymentSettings = deploymentSettings;
    }

    @Override
    public String getQueryString() {
        String query = requestURLHelper.getClientRequestedQueryString();
        return query == null ? "" : query;
    }

}
