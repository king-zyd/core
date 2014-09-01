package com.zyd.core.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
public class Mail {
    static final String CONTENT_TYPE_HTML = "text/html";

    private String contentType;

    private final List<String> toAddresses = new ArrayList<>();
    private final List<String> ccAddresses = new ArrayList<>();
    private final List<String> bccAddresses = new ArrayList<>();
    private String replyTo;
    private String subject;
    private String from;
    private String body;

    public void addTo(String toAddress) {
        toAddresses.add(toAddress);
    }

    public void addCC(String ccAddress) {
        ccAddresses.add(ccAddress);
    }

    public void addBCC(String bccAddress) {
        bccAddresses.add(bccAddress);
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public void clearToAddresses() {
        toAddresses.clear();
    }

    public List<String> getCCAddresses() {
        return ccAddresses;
    }

    public void clearCCAddresses() {
        ccAddresses.clear();
    }

    public List<String> getBCCAddresses() {
        return bccAddresses;
    }

    public void clearBCCAddresses() {
        bccAddresses.clear();
    }

    public void setHTMLBody(String body) {
        this.body = body;
        contentType = CONTENT_TYPE_HTML;
    }

    public void setPlainTextBody(String body) {
        this.body = body;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}
