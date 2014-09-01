package com.zyd.core.platform.web.site.cookie;

import com.zyd.core.platform.web.ControllerHelper;
import com.zyd.core.platform.web.site.session.RequireSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author neo
 */
public class CookieInterceptor extends HandlerInterceptorAdapter {
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = CookieInterceptor.class.getName() + ".CONTEXT_INITIALIZED";

    private final Logger logger = LoggerFactory.getLogger(CookieInterceptor.class);
    private CookieContext cookieContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!requireCookie(handler)) return true;

        if (!initialized(request)) {
            logger.debug("initialize cookieContext");

            Cookie[] cookies = request.getCookies();
            if (cookies != null)
                for (Cookie cookie : cookies) {
                    cookieContext.addCookie(cookie.getName(), cookie.getValue());
                }
            cookieContext.setHttpServletResponse(response);
            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }
        return true;
    }

    public boolean initialized(HttpServletRequest request) {
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        return Boolean.TRUE.equals(initialized);
    }

    boolean requireCookie(Object handler) {
        return ControllerHelper.findMethodOrClassLevelAnnotation(handler, RequireSession.class) != null
                || ControllerHelper.findMethodOrClassLevelAnnotation(handler, RequireCookie.class) != null;
    }

    @Inject
    public void setCookieContext(CookieContext cookieContext) {
        this.cookieContext = cookieContext;
    }
}
