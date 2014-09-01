package com.zyd.core.platform.web.filter;

import com.zyd.core.log.ActionLog;
import com.zyd.core.log.ActionLoggerImpl;
import com.zyd.core.log.TraceLogger;
import com.zyd.core.platform.web.DeploymentSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author neo
 */
public class PlatformFilter implements Filter {
    private static final String PARAM_FORCE_FLUSH_TRACE_LOG = "_trace";
    private final Logger logger = LoggerFactory.getLogger(PlatformFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        TraceLogger traceLogger = TraceLogger.get();
        ActionLoggerImpl actionLogger = ActionLoggerImpl.get();
        try {
            traceLogger.initialize();
            actionLogger.initialize();
            logger.debug("=== begin request processing ===");
            HttpServletRequest originalRequest = (HttpServletRequest) request;
            RequestWrapper requestWrapper = new RequestWrapper(originalRequest, DeploymentSettings.get());
            logRequest(requestWrapper, originalRequest);
            chain.doFilter(requestWrapper, response);
        } finally {
            logResponse((HttpServletResponse) response);
            logger.debug("=== finish request processing ===");
            traceLogger.cleanup(flushTraceLog(request));
            actionLogger.save();
        }
    }

    private void logResponse(HttpServletResponse response) {
        int status = response.getStatus();
        logger.debug("responseHTTPStatus={}", status);
        logHeaders(response);
        ActionLog actionLog = ActionLoggerImpl.get().currentActionLog();
        actionLog.setHTTPStatusCode(status);
    }

    private void logHeaders(HttpServletResponse response) {
        for (String name : response.getHeaderNames()) {
            logger.debug("[response-header] {}={}", name, response.getHeader(name));
        }
    }

    private void logRequest(RequestWrapper requestWrapper, HttpServletRequest originalRequest) throws IOException {
        logger.debug("originalRequestURL={}", originalRequest.getRequestURL());
        logger.debug("originalServerPort={}", originalRequest.getServerPort());
        logger.debug("originalContextPath={}", originalRequest.getContextPath());
        logger.debug("originalMethod={}", originalRequest.getMethod());
        logger.debug("dispatcherType={}", originalRequest.getDispatcherType());
        logger.debug("serverPort={}", requestWrapper.getServerPort());
        logger.debug("deployedContextPath={}", DeploymentSettings.get().getDeploymentContext());
        logger.debug("localPort={}", requestWrapper.getLocalPort());
        logHeaders(originalRequest);
        logParameters(originalRequest);
        logger.debug("remoteAddress={}", originalRequest.getRemoteAddr());

        if (requestWrapper.isPreLoadBody()) {
            logger.debug("body={}", requestWrapper.getOriginalBody());
        }

        ActionLog actionLog = ActionLoggerImpl.get().currentActionLog();
        actionLog.setHTTPMethod(originalRequest.getMethod());
    }

    private void logHeaders(HttpServletRequest request) {
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = (String) headers.nextElement();
            logger.debug("[header] {}={}", headerName, request.getHeader(headerName));
        }
    }

    private void logParameters(HttpServletRequest request) {
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            logger.debug("[param] {}={}", paramName, request.getParameter(paramName));
        }
    }

    private boolean flushTraceLog(ServletRequest request) {
        String traceParam = request.getParameter(PARAM_FORCE_FLUSH_TRACE_LOG);
        return "true".equals(traceParam);
    }

    @Override
    public void destroy() {
    }
}
