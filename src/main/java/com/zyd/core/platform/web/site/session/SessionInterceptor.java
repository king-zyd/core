package com.zyd.core.platform.web.site.session;

import com.zyd.core.collection.Key;
import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.web.ControllerHelper;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.platform.web.site.cookie.CookieContext;
import com.zyd.core.platform.web.site.cookie.CookieInterceptor;
import com.zyd.core.platform.web.site.cookie.CookieSpec;
import com.zyd.core.platform.web.site.session.provider.LocalSessionProvider;
import com.zyd.core.platform.web.site.session.provider.MemcachedSessionProvider;
import com.zyd.core.platform.web.site.session.provider.RedisSessionProvider;
import com.zyd.core.platform.web.site.session.provider.SessionProvider;
import com.zyd.core.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author neo
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    private static final CookieSpec<String> SESSION_ID = CookieSpec.spec(Key.stringKey("SessionId")).path("/").sessionScope().httpOnly();
    private static final CookieSpec<String> SECURE_SESSION_ID = CookieSpec.spec(Key.stringKey("SecureSessionId")).secure().path("/").sessionScope().httpOnly();

    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = SessionInterceptor.class.getName() + ".CONTEXT_INITIALIZED";
    private static final String BEAN_NAME_SESSION_PROVIDER = "sessionProvider";

    private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    private CookieContext cookieContext;
    private SessionContext sessionContext;
    private SecureSessionContext secureSessionContext;
    private SiteSettings siteSettings;
    private SessionProvider sessionProvider;
    private SpringObjectFactory springObjectFactory;
    private CookieInterceptor cookieInterceptor;

    @PostConstruct
    public void initialize() {
        SessionProviderType type = siteSettings.getSessionProviderType();
        if (SessionProviderType.MEMCACHED.equals(type)) {
            AssertUtils.assertHasText(siteSettings.getRemoteSessionServers(), "remote session server configuration is missing");
            springObjectFactory.registerSingletonBean(BEAN_NAME_SESSION_PROVIDER, MemcachedSessionProvider.class);
        } else if (SessionProviderType.REDIS.equals(type)) {
            AssertUtils.assertHasText(siteSettings.getRemoteSessionServers(), "remote session server configuration is missing");
            springObjectFactory.registerSingletonBean(BEAN_NAME_SESSION_PROVIDER, RedisSessionProvider.class);
        } else if (SessionProviderType.LOCAL.equals(type)) {
            springObjectFactory.registerSingletonBean(BEAN_NAME_SESSION_PROVIDER, LocalSessionProvider.class);
        } else {
            throw new IllegalStateException("unsupported session provider type, type=" + type);
        }
        sessionProvider = springObjectFactory.getBean(SessionProvider.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!requireSession(handler)) return true;

        // only process non-forwarded request, to make sure only init once per request
        if (!initialized(request)) {
            logger.debug("initialize sessionContext");

            AssertUtils.assertTrue(cookieInterceptor.initialized(request), "sessionInterceptor depends on cookieInterceptor, please check WebConfig");

            loadSession(sessionContext, SESSION_ID);
            if (request.isSecure()) {
                secureSessionContext.underSecureRequest();
                loadSession(secureSessionContext, SECURE_SESSION_ID);
            }
            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }
        return true;
    }

    private boolean initialized(HttpServletRequest request) {
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        return Boolean.TRUE.equals(initialized);
    }

    private boolean requireSession(Object handler) {
        return ControllerHelper.findMethodOrClassLevelAnnotation(handler, RequireSession.class) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        saveAllSessions(request);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // if some interceptor break the preHandle by returning false, all postHandle will be skipped.
        // by this way we want to try to save session on completion if possible
        // due to setCookies only works before view is rendered
        saveAllSessions(request);
    }

    private void saveAllSessions(HttpServletRequest request) {
        saveSession(sessionContext, SESSION_ID);
        if (request.isSecure()) {
            saveSession(secureSessionContext, SECURE_SESSION_ID);
        }
    }

    private void loadSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        String sessionId = cookieContext.getCookie(sessionCookie);
        if (sessionId != null) {
            String sessionData = sessionProvider.getAndRefreshSession(sessionId);
            if (sessionData != null) {
                sessionContext.setId(sessionId);
                sessionContext.loadSessionData(sessionData);
            } else {
                logger.debug("can not find session, generate new sessionId to replace old one");
                sessionContext.requireNewSessionId();
            }
        }
    }

    private void saveSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        if (sessionContext.changed()) {
            if (sessionContext.invalidated()) {
                deleteSession(sessionContext, sessionCookie);
            } else {
                persistSession(sessionContext, sessionCookie);
            }
            sessionContext.saved();
        }
    }

    private void deleteSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        String sessionId = sessionContext.getId();
        if (sessionId == null) {
            // session was not persisted, nothing is required
            return;
        }
        sessionProvider.clearSession(sessionId);
        cookieContext.deleteCookie(sessionCookie);
    }

    private void persistSession(SessionContext sessionContext, CookieSpec<String> sessionCookie) {
        String sessionId = sessionContext.getId();
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            sessionContext.setId(sessionId);
            cookieContext.setCookie(sessionCookie, sessionId);
        }
        sessionProvider.saveSession(sessionId, sessionContext.getSessionData());
    }

    @Inject
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Inject
    public void setSecureSessionContext(SecureSessionContext secureSessionContext) {
        this.secureSessionContext = secureSessionContext;
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setCookieContext(CookieContext cookieContext) {
        this.cookieContext = cookieContext;
    }

    @Inject
    public void setSpringObjectFactory(SpringObjectFactory springObjectFactory) {
        this.springObjectFactory = springObjectFactory;
    }

    @Inject
    public void setCookieInterceptor(CookieInterceptor cookieInterceptor) {
        this.cookieInterceptor = cookieInterceptor;
    }
}
