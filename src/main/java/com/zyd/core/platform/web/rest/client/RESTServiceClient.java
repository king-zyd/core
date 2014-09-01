package com.zyd.core.platform.web.rest.client;

import com.zyd.core.http.HTTPClient;
import com.zyd.core.http.HTTPConstants;
import com.zyd.core.http.HTTPRequest;
import com.zyd.core.http.HTTPResponse;
import com.zyd.core.http.HTTPStatusCode;
import com.zyd.core.json.JSONBinder;
import com.zyd.core.platform.exception.InvalidRequestException;
import com.zyd.core.platform.exception.RemoteServiceException;
import com.zyd.core.platform.exception.ResourceNotFoundException;
import com.zyd.core.platform.web.request.RequestContextInterceptor;
import com.zyd.core.platform.web.rest.security.ClientAuthorizationInterceptor;
import com.zyd.core.platform.web.rest.security.RequestMessage;
import com.zyd.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

/**
 * @author neo
 */
public class RESTServiceClient {
    private final Logger logger = LoggerFactory.getLogger(RESTServiceClient.class);

    private String requestId;
    private String clientId;
    private String secretKey;
    private HTTPClient httpClient;
    private boolean validateStatusCode;

    public <T> T get(String uri, Class<T> responseClass) {
        HTTPResponse response = execute(HTTPRequest.get(uri).request());
        return JSONBinder.binder(responseClass).fromJSON(response.responseText());
    }

    @SuppressWarnings("unchecked")
    public <T, U> T post(String uri, Class<T> responseClass, U request) {
        String body = JSONBinder.binder((Class<U>) request.getClass()).toJSON(request);
        HTTPRequest httpRequest = HTTPRequest.post(uri).text(body, HTTPConstants.CONTENT_TYPE_JSON).request();

        HTTPResponse response = execute(httpRequest);
        return JSONBinder.binder(responseClass).fromJSON(response.responseText());
    }

    @SuppressWarnings("unchecked")
    public <T, U> T put(String uri, Class<T> responseClass, U request) {
        String body = JSONBinder.binder((Class<U>) request.getClass()).toJSON(request);
        HTTPRequest httpRequest = HTTPRequest.put(uri).text(body, HTTPConstants.CONTENT_TYPE_JSON).request();

        HTTPResponse response = execute(httpRequest);
        return JSONBinder.binder(responseClass).fromJSON(response.responseText());
    }

    public <T, U> T delete(String uri, Class<T> responseClass) {
        HTTPResponse response = execute(HTTPRequest.delete(uri).request());
        return JSONBinder.binder(responseClass).fromJSON(response.responseText());
    }

    public HTTPResponse execute(HTTPRequest request) {
        prepareRequestHeaders(request);
        HTTPResponse response = httpClient.execute(request);

        if (validateStatusCode) {
            validateStatusCode(response);
        }
        return response;
    }

    private void prepareRequestHeaders(HTTPRequest request) {
        request.accept(HTTPConstants.CONTENT_TYPE_JSON);
        request.addHeader(RequestContextInterceptor.HEADER_REQUEST_ID, getRequestId());

        if (clientId != null) {
            signRequestMessage(request);
        }
    }

    private void signRequestMessage(HTTPRequest request) {
        request.addHeader(ClientAuthorizationInterceptor.HEADER_CLIENT_ID, clientId);

        Date now = new Date();
        request.addHeader(ClientAuthorizationInterceptor.HEADER_TIMESTAMP, String.valueOf(now.getTime()));

        RequestMessage message = new RequestMessage();

        message.setBody(request.body());
        message.setMethod(request.method().name());
        message.setTimestamp(now);
        message.setURI(request.url());

        request.addHeader(ClientAuthorizationInterceptor.HEADER_CLIENT_SIGNATURE, message.sign(secretKey));
    }

    private void validateStatusCode(HTTPResponse response) {
        HTTPStatusCode statusCode = response.statusCode();
        logger.debug("response status code => {}", statusCode.code());
        if (statusCode.code() == HTTPConstants.SC_BAD_REQUEST) {
            throw new InvalidRequestException("invalid request, responseText=" + response.responseText());
        }
        if (statusCode.code() == HTTPConstants.SC_NOT_FOUND) {
            throw new ResourceNotFoundException("resource not found, responseText=" + response.responseText());
        }
        if (!statusCode.isSuccess()) {
            throw new RemoteServiceException("failed to execute service, responseText=" + response.responseText() + ", statusCode=" + statusCode.code(), statusCode.code());
        }
    }

    String getRequestId() {
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    public void setClientSignatureKey(String clientId, String secretKey) {
        this.clientId = clientId;
        this.secretKey = secretKey;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setValidateStatusCode(boolean validateStatusCode) {
        this.validateStatusCode = validateStatusCode;
    }

    @Inject
    public void setHTTPClient(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }
}
