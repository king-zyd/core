package com.zyd.core.platform.web.filter;

import com.zyd.core.http.HTTPConstants;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.util.AssertUtils;
import com.zyd.core.util.CharacterEncodings;
import com.zyd.core.util.IOUtils;
import com.zyd.core.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author neo
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private static final String HEADER_X_FORWARDED_PROTO = "x-forwarded-proto";
    private ServletInputStream inputStream;
    private String originalBody;
    private String body;
    private BufferedReader reader;
    private final String scheme;
    private final int serverPort;

    public RequestWrapper(HttpServletRequest request, DeploymentSettings deploymentSettings) throws IOException {
        super(request);

        preLoadBody(request);
        scheme = parseProxyScheme(request);
        serverPort = parseProxyServerPort(request, deploymentSettings);
    }

    private int parseProxyServerPort(HttpServletRequest originalRequest, DeploymentSettings deploymentSettings) {
        Integer deploymentHTTPPort = deploymentSettings.getHTTPPort();
        if (HTTPConstants.SCHEME_HTTP.equals(scheme) && deploymentHTTPPort != null) {
            return deploymentHTTPPort;
        }

        Integer deploymentHTTPSPort = deploymentSettings.getHTTPSPort();
        if (HTTPConstants.SCHEME_HTTPS.equals(scheme) && deploymentHTTPSPort != null) {
            return deploymentHTTPSPort;
        }

        return originalRequest.getServerPort();
    }

    private String parseProxyScheme(HttpServletRequest originalRequest) {
        String forwardedProtocol = originalRequest.getHeader(HEADER_X_FORWARDED_PROTO);
        if (StringUtils.hasText(forwardedProtocol)) {
            return forwardedProtocol.toLowerCase();
        }
        return originalRequest.getScheme();
    }

    private void preLoadBody(HttpServletRequest request) throws IOException {
        if (isPreLoadBody()) {
            Charset charset = Charset.forName(getCharacterEncoding());
            ServletInputStream originalInputStream = request.getInputStream();
            AssertUtils.assertNotNull(originalInputStream, "POST or PUT should have input steam as body");
            byte[] bodyBytes = IOUtils.bytes(originalInputStream);
            originalBody = new String(bodyBytes, charset);
            body = getParameter("_body");
            if (body == null)
                body = originalBody;
            inputStream = new RequestCachingInputStream(body.getBytes(charset));
        }
    }

    @Override
    public final String getContentType() {
        String overrideContentType = getParameter("_contentType");
        if (overrideContentType != null)
            return overrideContentType;
        return super.getContentType();
    }

    @Override
    public String getHeader(String name) {
        if (isAcceptHeader(name)) {
            String overrideAccept = getParameter("_accept");
            if (overrideAccept != null)
                return overrideAccept;
        }
        return super.getHeader(name);
    }

    private boolean isAcceptHeader(String name) {
        // server side all headers are in lower case
        return HTTPConstants.HEADER_ACCEPT.equalsIgnoreCase(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        // server side all headers are in lower case
        if (isAcceptHeader(name)) {
            String overrideAccept = getParameter("_accept");
            if (overrideAccept != null) {
                Vector<String> headers = new Vector<>();
                headers.add(overrideAccept);
                return headers.elements();
            }
        }
        return super.getHeaders(name);
    }

    @Override
    public String getMethod() {
        String overrideMethod = getParameter("_method");
        if (overrideMethod != null)
            return overrideMethod;
        return super.getMethod();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null)
            return inputStream;
        return super.getInputStream();
    }

    @Override
    public final String getCharacterEncoding() {
        String defaultEncoding = super.getCharacterEncoding();
        return defaultEncoding != null ? defaultEncoding : CharacterEncodings.UTF_8;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(inputStream, getCharacterEncoding()));
        }
        return reader;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public boolean isSecure() {
        return HTTPConstants.SCHEME_HTTPS.equals(getScheme());
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    String getOriginalBody() {
        return originalBody;
    }

    public String getBody() {
        AssertUtils.assertFalse(isMultipart(), "should not try to getBody for multipart request");
        return body;
    }

    final boolean isMultipart() {
        String contentType = getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    final boolean containsBody() {
        String originalMethod = super.getMethod().toUpperCase();
        return HTTPConstants.METHOD_POST.equals(originalMethod) || HTTPConstants.METHOD_PUT.equals(originalMethod);
    }

    final boolean isPreLoadBody() {
        return containsBody() && !isMultipart();
    }
}
