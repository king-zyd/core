package com.zyd.core.platform.web.rest.security;

import com.zyd.core.http.HTTPConstants;
import com.zyd.core.util.DateUtils;
import com.zyd.core.util.TimeLength;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class RequestMessageTest {
    RequestMessage requestMessage;

    @Before
    public void createMessageSigner() {
        requestMessage = new RequestMessage();
    }

    @Test
    public void constructToBeSignedMessage() {
        requestMessage.setURI("http://localhost");
        requestMessage.setBody("{test:1}");
        requestMessage.setMethod(HTTPConstants.METHOD_POST);
        requestMessage.setTimestamp(new Date());
        String message = requestMessage.constructToBeSignedMessage();

        assertThat(message, containsString("uri=http://localhost&"));
        assertThat(message, containsString("body={test:1}&"));
    }

    @Test
    public void constructToBeSignedMessageWithNullBody() {
        requestMessage.setURI("http://localhost");
        requestMessage.setBody(null);
        requestMessage.setMethod(HTTPConstants.METHOD_GET);
        requestMessage.setTimestamp(new Date());
        String message = requestMessage.constructToBeSignedMessage();

        assertThat(message, containsString("uri=http://localhost&method=GET&body=&"));
    }

    @Test
    public void isExpired() {
        Date clientTime = DateUtils.date(2012, Calendar.SEPTEMBER, 19, 16, 32, 0);
        requestMessage.setTimestamp(clientTime);
        Assert.assertFalse(requestMessage.isExpired(DateUtils.date(2012, Calendar.SEPTEMBER, 19, 16, 33, 0), TimeLength.minutes(5)));
        Assert.assertTrue(requestMessage.isExpired(DateUtils.date(2012, Calendar.SEPTEMBER, 19, 16, 37, 1), TimeLength.minutes(5)));
    }
}
