package com.zyd.core.platform.web.site.cookie;

import com.zyd.core.collection.Key;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

/**
 * @author neo
 */
public class CookieContextTest extends EasyMockSupport {
    CookieContext cookieContext;
    Cookie cookie;

    @Before
    public void prepare() {
        cookieContext = new CookieContext();
        cookieContext.setHttpServletResponse(new MockHttpServletResponse());
        cookie = createMock(Cookie.class);
    }

    @Test
    public void setCookieHTTPOnly() {
        CookieSpec<String> spec = CookieSpec.spec(Key.stringKey("test")).httpOnly();
        cookie.setHttpOnly(true);
        replayAll();

        cookieContext.setCookieAttributes(spec, cookie);

        verifyAll();
    }

    @Test
    public void setCookie() {
        String value = "setCookieValue";
        CookieSpec<String> spec = CookieSpec.spec(Key.stringKey("setCookieName")).httpOnly();
        cookieContext.setCookie(spec, value);
        Assert.assertEquals(value, cookieContext.getCookie(spec));
    }
}
