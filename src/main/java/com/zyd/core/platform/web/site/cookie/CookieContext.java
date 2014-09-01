package com.zyd.core.platform.web.site.cookie;

import com.zyd.core.collection.KeyMap;
import com.zyd.core.collection.TypeConversionException;
import com.zyd.core.collection.TypeConverter;
import com.zyd.core.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author neo
 */
public class CookieContext {
    private static final int DELETE_COOKIE_MAX_AGE = 0;
    private final Logger logger = LoggerFactory.getLogger(CookieContext.class);

    private HttpServletResponse httpServletResponse;

    private final KeyMap cookies = new KeyMap();
    private final TypeConverter typeConverter = new TypeConverter();

    public <T> T getCookie(CookieSpec<T> spec) {
        try {
            return cookies.get(spec.nameKey());
        } catch (TypeConversionException e) {
            logger.warn("failed to convert cookie value", e);
            return null;
        }
    }

    public <T> void setCookie(CookieSpec<T> spec, T value) {
        Cookie cookie = new Cookie(spec.nameKey().name(), typeConverter.toString(value));
        setCookieAttributes(spec, cookie);

        AssertUtils.assertNotNull(httpServletResponse, "response is not injected, please check cookieInterceptor is added in WebConfig");
        httpServletResponse.addCookie(cookie);
        addCookie(cookie.getName(), cookie.getValue());
    }

    <T> void setCookieAttributes(CookieSpec<T> spec, Cookie cookie) {
        if (spec.pathAssigned())
            cookie.setPath(spec.path());
        if (spec.httpOnlyAssigned())
            cookie.setHttpOnly(spec.isHTTPOnly());
        if (spec.secureAssigned())
            cookie.setSecure(spec.isSecure());
        if (spec.maxAgeAssigned())
            cookie.setMaxAge((int) spec.maxAge().toSeconds());
    }

    public <T> void deleteCookie(CookieSpec spec) {
        Cookie cookie = new Cookie(spec.nameKey().name(), null);
        cookie.setMaxAge(DELETE_COOKIE_MAX_AGE);
        cookie.setPath(spec.path());
        AssertUtils.assertNotNull(httpServletResponse, "response is not injected, please check cookieInterceptor is added in WebConfig");
        httpServletResponse.addCookie(cookie);
    }

    void addCookie(String name, String value) {
        cookies.putString(name, value);
    }

    void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
