package com.zyd.core.platform.web.request;

import com.zyd.core.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author neo
 */
public class RemoteAddress {
    static final String HTTP_HEADER_X_FORWARDED_FOR = "x-forwarded-for";
    static final String HTTP_HEADER_CLIENT_IP = "client-ip";

    public static RemoteAddress create(HttpServletRequest request) {
        String directRemoteAddress = request.getRemoteAddr();
        String clientIpHeader = request.getHeader(HTTP_HEADER_CLIENT_IP); // netscaler doesn't use x-forwarded-for
        String xForwardedFor = request.getHeader(HTTP_HEADER_X_FORWARDED_FOR);
        String forwardIPHeader = StringUtils.hasText(clientIpHeader) ? clientIpHeader : xForwardedFor;
        return new RemoteAddress(directRemoteAddress, forwardIPHeader);
    }

    private final String remoteAddress;
    // for original ip if there is proxy
    private final String xForwardedFor;

    RemoteAddress(String remoteAddress, String xForwardedFor) {
        this.remoteAddress = remoteAddress;
        this.xForwardedFor = xForwardedFor;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getXForwardedFor() {
        return xForwardedFor;
    }

    /**
     * get actual client ip, being aware of proxy
     *
     * @return the ip of client from request
     */
    public String getClientIP() {
        if (!StringUtils.hasText(xForwardedFor))
            return remoteAddress;
        int index = xForwardedFor.indexOf(',');
        if (index > 0)
            return xForwardedFor.substring(0, index);
        return xForwardedFor;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(xForwardedFor)) {
            builder.append(xForwardedFor).append(", ");
        }
        builder.append(remoteAddress);
        return builder.toString();
    }
}
