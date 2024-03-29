package com.zyd.core.http;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;

/**
 * @author neo
 */
public class HTTPClientTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    HTTPClient client;

    @Before
    public void createHTTPClient() {
        client = new HTTPClient();
    }

    @Test
    public void receiveClientErrorStatusCode() {
        exception.expect(HTTPException.class);
        exception.expectMessage(JUnitMatchers.containsString(String.valueOf(HttpStatus.SC_FORBIDDEN)));
        client.validateStatusCode(new HTTPStatusCode(HTTPConstants.SC_FORBIDDEN));
    }

    @Test
    public void receiveRedirectStatusCode() {
        client.validateStatusCode(new HTTPStatusCode(HTTPConstants.SC_MOVED_PERMANENTLY));
    }

    @Test
    public void receiveSuccessStatusCode() {
        client.validateStatusCode(new HTTPStatusCode(HTTPConstants.SC_ACCEPTED));
    }
}
