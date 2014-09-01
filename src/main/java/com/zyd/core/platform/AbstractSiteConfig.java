package com.zyd.core.platform;

import com.zyd.core.platform.web.exception.ExceptionInterceptor;
import com.zyd.core.platform.web.site.SiteControllerAdvice;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.platform.web.site.cdn.CDNSettings;
import com.zyd.core.platform.web.site.cdn.DefaultCDNSettings;
import com.zyd.core.platform.web.site.cookie.CookieContext;
import com.zyd.core.platform.web.site.cookie.CookieInterceptor;
import com.zyd.core.platform.web.site.exception.ErrorPageModelBuilder;
import com.zyd.core.platform.web.site.exception.SiteExceptionInterceptor;
import com.zyd.core.platform.web.site.handler.ResourceNotFoundHandlerMapping;
import com.zyd.core.platform.web.site.layout.ModelContext;
import com.zyd.core.platform.web.site.scheme.HTTPSchemeEnforceInterceptor;
import com.zyd.core.platform.web.site.session.SecureSessionContext;
import com.zyd.core.platform.web.site.session.SessionContext;
import com.zyd.core.platform.web.site.session.SessionInterceptor;
import com.zyd.core.platform.web.site.view.DefaultFreeMarkerConfigurer;
import com.zyd.core.platform.web.site.view.DefaultFreemarkerView;
import com.zyd.core.platform.web.site.view.DefaultFreemarkerViewResolver;
import com.zyd.core.platform.web.site.view.DefaultRedirectViewResolver;
import com.zyd.core.util.CharacterEncodings;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.util.Properties;

/**
 * @author chi
 */
public abstract class AbstractSiteConfig extends AbstractWebConfig {
    @Bean
    public CDNSettings cdnSettings() {
        return new DefaultCDNSettings();
    }

    @Bean
    public SiteSettings siteSettings() {
        return new SiteSettings();
    }

    @Bean
    DefaultFreeMarkerConfigurer freeMarkerConfigurer() {
        DefaultFreeMarkerConfigurer config = new DefaultFreeMarkerConfigurer();
        config.setTemplateLoaderPath("/");
        Properties settings = new Properties();
        settings.setProperty(Configuration.DEFAULT_ENCODING_KEY, CharacterEncodings.UTF_8);
        settings.setProperty(Configuration.URL_ESCAPING_CHARSET_KEY, CharacterEncodings.UTF_8);
        settings.setProperty(Configuration.NUMBER_FORMAT_KEY, "0.##");
        settings.setProperty(Configuration.TEMPLATE_EXCEPTION_HANDLER_KEY, "rethrow");
        config.setFreemarkerSettings(settings);
        return config;
    }

    @Bean
    protected DefaultFreemarkerViewResolver freeMarkerViewResolver() {
        DefaultFreemarkerViewResolver resolver = new DefaultFreemarkerViewResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setViewClass(DefaultFreemarkerView.class);
        resolver.setExposeSpringMacroHelpers(false);
        resolver.setExposeRequestAttributes(true);
        resolver.setAllowRequestOverride(true);
        return resolver;
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    CookieContext cookieContext() {
        return new CookieContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    SessionContext sessionContext() {
        return new SessionContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    SecureSessionContext secureSessionContext() {
        return new SecureSessionContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    ModelContext modelContextContext() {
        return new ModelContext();
    }

    @Bean
    DefaultRedirectViewResolver redirectViewResolver() {
        return new DefaultRedirectViewResolver();
    }

    @Bean
    ErrorPageModelBuilder errorPageModelBuilder() {
        return new ErrorPageModelBuilder();
    }

    @Bean
    ResourceNotFoundHandlerMapping resourceNotFoundHandlerMapping() {
        return new ResourceNotFoundHandlerMapping();
    }

    @Bean
    SiteControllerAdvice siteControllerAdvice() {
        return new SiteControllerAdvice();
    }

    @Bean
    public ExceptionInterceptor exceptionInterceptor() {
        return new SiteExceptionInterceptor();
    }

    @Bean
    public CookieInterceptor cookieInterceptor() {
        return new CookieInterceptor();
    }

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    public HTTPSchemeEnforceInterceptor httpSchemeEnforceInterceptor() {
        return new HTTPSchemeEnforceInterceptor();
    }
}
