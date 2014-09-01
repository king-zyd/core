package com.zyd.core.platform.web.rest.security;

import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.exception.UserAuthorizationException;
import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.ControllerHelper;
import com.zyd.core.platform.web.filter.RequestWrapper;
import com.zyd.core.platform.web.request.RequestContextImpl;
import com.zyd.core.platform.web.request.RequestContextInterceptor;
import com.zyd.core.util.AssertUtils;
import com.zyd.core.util.Convert;
import com.zyd.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author neo
 */
public class ClientAuthorizationInterceptor extends HandlerInterceptorAdapter {
    public static final String HEADER_CLIENT_ID = "client-id";
    public static final String HEADER_TIMESTAMP = "timestamp";
    public static final String HEADER_CLIENT_SIGNATURE = "client-signature";

    private final Logger logger = LoggerFactory.getLogger(ClientAuthorizationInterceptor.class);

    private ClientServiceFactory clientServiceFactory;
    private SpringObjectFactory springObjectFactory;
    private RequestContextImpl requestContext;
    private ClientService clientService;
    private RuntimeSettings runtimeSettings;
    private RequestContextInterceptor requestContextInterceptor;

    @PostConstruct
    public void initialize() {
        AssertUtils.assertNotNull(clientServiceFactory, "clientServiceFactory must be set in the @Bean method of ClientAuthorizationInterceptor");
        clientService = clientServiceFactory.create(springObjectFactory);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AssertUtils.assertTrue(requestContextInterceptor.initialized(request), "clientAuthorizationInterceptor depends on requestContextInterceptor, please check WebConfig");

        Protected annotation = ControllerHelper.findMethodOrClassLevelAnnotation(handler, Protected.class);
        if (annotation != null) {
            authenticate(request, annotation);
        }

        return true;
    }

    private void authenticate(HttpServletRequest request, Protected annotation) {
        String clientId = request.getHeader(HEADER_CLIENT_ID);
        if (shouldAuthenticate(clientId)) {
            authenticateClient((RequestWrapper) request, annotation, clientId);
        } else {
            logger.debug("authenticate skipped");
        }
    }

    private void authenticateClient(RequestWrapper request, Protected annotation, String clientId) {
        if (!StringUtils.hasText(clientId)) throw new UserAuthorizationException("client-id header is required");
        logger.debug("authenticate client, clientId=" + clientId);
        verifyClientSignature(request, clientId);

        requestContext.setClientId(clientId);

        clientService.authorizeClient(clientId, annotation.system(), annotation.operation());
    }

    boolean shouldAuthenticate(String clientId) {
        if (StringUtils.hasText(clientId)) return true;
        if (RuntimeEnvironment.DEV.equals(runtimeSettings.getEnvironment()))
            return false;   // only DEV environment supports bypass client auth
        return true;
    }

    private void verifyClientSignature(RequestWrapper request, String clientId) {
        Date clientTimestamp = getClientTimestamp(request);
        String signature = getSignature(request);

        RequestMessage message = new RequestMessage();
        message.setURI(requestContext.getClientRequestedFullURLWithQueryString());
        message.setMethod(request.getMethod());
        message.setTimestamp(clientTimestamp);

        String body = request.getBody();
        message.setBody(body);

        clientService.authenticateClient(clientId, message, signature);
    }

    private String getSignature(HttpServletRequest request) {
        String signature = request.getHeader(HEADER_CLIENT_SIGNATURE);
        if (!StringUtils.hasText(signature))
            throw new UserAuthorizationException("client-signature header is required");
        return signature;
    }

    Date getClientTimestamp(HttpServletRequest request) {
        String timestamp = request.getHeader(HEADER_TIMESTAMP);
        if (!StringUtils.hasText(timestamp))
            throw new UserAuthorizationException("timestamp header is required");

        Long clientTimestamp = Convert.toLong(timestamp, null);

        if (clientTimestamp == null) {
            Date date = Convert.toDateTime(timestamp, null);
            if (date == null)
                throw new UserAuthorizationException("timestamp must be in format: " + Convert.DATE_FORMAT_DATETIME + ", or in million seconds");
            return date;
        } else {
            return new Date(clientTimestamp);
        }
    }


    public void setClientServiceFactory(ClientServiceFactory clientServiceFactory) {
        this.clientServiceFactory = clientServiceFactory;
    }

    @Inject
    public void setRequestContext(RequestContextImpl requestContext) {
        this.requestContext = requestContext;
    }

    @Inject
    public void setRuntimeSettings(RuntimeSettings runtimeSettings) {
        this.runtimeSettings = runtimeSettings;
    }

    @Inject
    public void setSpringObjectFactory(SpringObjectFactory springObjectFactory) {
        this.springObjectFactory = springObjectFactory;
    }

    @Inject
    public void setRequestContextInterceptor(RequestContextInterceptor requestContextInterceptor) {
        this.requestContextInterceptor = requestContextInterceptor;
    }
}
