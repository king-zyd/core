package com.zyd.core.platform;

import com.zyd.core.json.JSONBinder;
import com.zyd.core.platform.context.Messages;
import com.zyd.core.platform.management.web.ManagementControllerConfig;
import com.zyd.core.platform.monitor.web.MonitorControllerConfig;
import com.zyd.core.platform.monitor.web.TrackInterceptor;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.exception.ExceptionTrackingHandler;
import com.zyd.core.platform.web.filter.PlatformFilter;
import com.zyd.core.platform.web.form.AnnotationFormArgumentResolver;
import com.zyd.core.platform.web.request.RequestContextImpl;
import com.zyd.core.platform.web.request.RequestContextInterceptor;
import com.zyd.core.platform.web.rest.exception.ErrorResponseBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.method.annotation.ErrorsMethodArgumentResolver;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chi
 */
@EnableWebMvc
@Import({MonitorControllerConfig.class, ManagementControllerConfig.class})
public abstract class AbstractWebConfig extends WebMvcConfigurerAdapter implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", DispatcherServlet.class);
        dispatcher.setInitParameter("contextConfigLocation", getClass().getName());
        dispatcher.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        dispatcher.setInitParameter("contextInitializerClasses", DefaultAppContextInitializer.class.getName());
        dispatcher.addMapping("/*");
        dispatcher.setLoadOnStartup(1);
        servletContext.addFilter("platformFilter", new PlatformFilter()).addMappingForUrlPatterns(null, false, "/*");
    }

    @Bean
    public DeploymentSettings deploymentSettings() {
        return DeploymentSettings.get();
    }

    @Inject
    Messages messages;

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messages);
        validator.afterPropertiesSet();
        return validator;
    }

    @Bean
    public List<HttpMessageConverter<?>> messageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        ArrayList<MediaType> textTypes = new ArrayList<>();
        textTypes.add(MediaType.TEXT_PLAIN);
        textTypes.add(MediaType.TEXT_HTML);
        textTypes.add(MediaType.TEXT_XML);
        textTypes.add(MediaType.APPLICATION_XML);
        textTypes.add(MediaType.APPLICATION_JSON);
        stringConverter.setSupportedMediaTypes(textTypes);
        converters.add(stringConverter);
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(JSONBinder.getObjectMapper());
        converters.add(jsonConverter);
        return converters;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(messageConverters());
    }

    @Bean
    public ExceptionTrackingHandler exceptionTrackingHandler() {
        return new ExceptionTrackingHandler();
    }

    @Bean
    ErrorResponseBuilder errorResponseBuilder() {
        return new ErrorResponseBuilder();
    }

    @Inject
    ConfigurableBeanFactory beanFactory;

    /**
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // Annotation-based argument resolution
        resolvers.add(new RequestParamMethodArgumentResolver(beanFactory, false));
        resolvers.add(new RequestParamMapMethodArgumentResolver());
        resolvers.add(new PathVariableMethodArgumentResolver());
        resolvers.add(new ServletModelAttributeMethodProcessor(false));
        resolvers.add(new RequestResponseBodyMethodProcessor(messageConverters()));
        resolvers.add(new RequestPartMethodArgumentResolver(messageConverters()));
        resolvers.add(new RequestHeaderMethodArgumentResolver(beanFactory));
        resolvers.add(new RequestHeaderMapMethodArgumentResolver());
        resolvers.add(new ExpressionValueMethodArgumentResolver(beanFactory));
        resolvers.add(new AnnotationFormArgumentResolver());

        // Type-based argument resolution
        resolvers.add(new MapMethodProcessor());
        resolvers.add(new ErrorsMethodArgumentResolver());
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    RequestContextImpl requestContext() {
        return new RequestContextImpl();
    }

    @Bean
    public RequestContextInterceptor requestContextInterceptor() {
        return new RequestContextInterceptor();
    }

    @Bean
    public TrackInterceptor trackInterceptor() {
        return new TrackInterceptor();
    }
}
