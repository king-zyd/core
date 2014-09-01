package com.suryani.site;

import com.zyd.core.platform.AbstractSiteConfig;
import com.zyd.core.platform.PlatformScopeResolver;
import com.zyd.core.platform.web.site.SiteSettings;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 * @author neo
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class, scopeResolver = PlatformScopeResolver.class)
@EnableAspectJAutoProxy
@EnableTransactionManagement(proxyTargetClass = true)
public class WebConfig extends AbstractSiteConfig {
    @Inject
    ServletContext servletContext;

    @Override
    public SiteSettings siteSettings() {
        SiteSettings settings = new SiteSettings();
        settings.setErrorPage("forward:/error/internal-error");
        settings.setResourceNotFoundPage("forward:/error/resource-not-found");
        return settings;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(exceptionInterceptor());
        registry.addInterceptor(requestContextInterceptor());
        registry.addInterceptor(httpSchemeEnforceInterceptor());
        registry.addInterceptor(cookieInterceptor());
        registry.addInterceptor(sessionInterceptor());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/home");
    }
}
