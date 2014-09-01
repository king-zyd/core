package com.zyd.core.platform.web.rest.security;

import com.zyd.core.crypto.HMAC;
import com.zyd.core.util.Convert;
import com.zyd.core.util.DateRangeUtils;
import com.zyd.core.util.EncodingUtils;
import com.zyd.core.util.TimeLength;

import java.util.Date;

/**
 * @author neo
 */
public class RequestMessage {
    private String uri;
    private String method;
    private String body;
    private Date timestamp;

    public String sign(String secretKey) {
        String message = constructToBeSignedMessage();

        HMAC hmac = new HMAC();
        hmac.setHash(HMAC.Hash.SHA1);
        hmac.setSecretKey(EncodingUtils.decodeBase64(secretKey));
        return EncodingUtils.base64(hmac.digest(message));
    }


    public boolean isExpired(Date serverTimestamp, TimeLength validityPeriod) {
        double secondsGap = DateRangeUtils.secondsBetween(timestamp, serverTimestamp);
        return secondsGap > validityPeriod.toSeconds();
    }

    String constructToBeSignedMessage() {
        StringBuilder builder = new StringBuilder(300);
        builder.append("uri=").append(uri).append("&method=")
                .append(method.toUpperCase()).append("&body=");
        if (body != null) builder.append(body);
        builder.append("&timestamp=").append(timestamp.getTime());
        return builder.toString();
    }

    //TODO(chi): remove after client upgrade
    public String signCompatible(String secretKey) {
        String message = constructToBeSignedCompatibleMessage();

        HMAC hmac = new HMAC();
        hmac.setHash(HMAC.Hash.SHA1);
        hmac.setSecretKey(EncodingUtils.decodeBase64(secretKey));
        return EncodingUtils.base64(hmac.digest(message));
    }

    String constructToBeSignedCompatibleMessage() {
        StringBuilder builder = new StringBuilder(300);
        builder.append("uri=").append(uri).append("&method=")
                .append(method.toUpperCase()).append("&body=");
        if (body != null) builder.append(body);
        builder.append("&timestamp=").append(Convert.toString(timestamp, Convert.DATE_FORMAT_DATETIME));
        return builder.toString();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
