package com.zyd.core.platform.web.site.view;

import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.url.URLBuilder;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

/**
 * @author neo
 */
public class DefaultRedirectViewResolver extends UrlBasedViewResolver {
    public DefaultRedirectViewResolver() {
        setOrder(Ordered.HIGHEST_PRECEDENCE);
        setRedirectHttp10Compatible(false);
        setRedirectContextRelative(false);
        setViewClass(RedirectView.class);
    }

    // modified according to parent method
    @Override
    protected View createView(String viewName, Locale locale) throws Exception {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectURL = viewName.substring(REDIRECT_URL_PREFIX.length());
            // always use contextRelative = false, and handle url according to SiteSettings
            URLBuilder builder = new URLBuilder();
            builder.setContextPath(DeploymentSettings.get().getDeploymentContext());
            builder.setLogicalURL(redirectURL);
            String targetURL = builder.buildRelativeURL();

            RedirectView view = new RedirectView(targetURL, isRedirectContextRelative(), isRedirectHttp10Compatible());
            view.setExposeModelAttributes(false);
            return applyLifecycleMethods(viewName, view);
        }

        return null;
    }

    private View applyLifecycleMethods(String viewName, AbstractView view) {
        return (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
    }

    boolean useHTTP10Redirect() {
        return isRedirectHttp10Compatible();
    }

    boolean useSpringRelativeRedirect() {
        return isRedirectContextRelative();
    }
}
