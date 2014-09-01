package com.zyd.core.platform;

import com.zyd.core.platform.service.ManagedService;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.type.AnnotationMetadata;

import javax.inject.Singleton;
import java.util.Map;

/**
 * Make scanned component as prototype by default
 * <p/>
 * port from org.springframework.context.annotation.AnnotationScopeMetadataResolver
 *
 * @author neo
 */
public class PlatformScopeResolver implements ScopeMetadataResolver {
    @Override
    public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
        ScopeMetadata metadata = new ScopeMetadata();
        metadata.setScopeName(BeanDefinition.SCOPE_PROTOTYPE);
        if (definition instanceof AnnotatedBeanDefinition) {
            validateManagedServiceAnnotation((AnnotatedBeanDefinition) definition);

            resolveScopeAnnotation((AnnotatedBeanDefinition) definition, metadata);
        }
        return metadata;
    }

    private void validateManagedServiceAnnotation(AnnotatedBeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        try {
            Class<?> beanClass = Class.forName(beanClassName);
            if (ManagedService.class.isAssignableFrom(beanClass)) {
                boolean isSingleton = definition.getMetadata().isAnnotated(Singleton.class.getName());
                if (!isSingleton)
                    throw new IllegalArgumentException("ManagedService must have @Singleton in class, class=" + beanClass.getName());
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("can not load bean class, className=" + beanClassName, e);
        }
    }

    private void resolveScopeAnnotation(AnnotatedBeanDefinition definition, ScopeMetadata metadata) {
        boolean isSingleton = annotatedAsSingleton(definition.getMetadata());
        if (isSingleton)
            metadata.setScopeName(BeanDefinition.SCOPE_SINGLETON);

        Map<String, Object> attributes = definition.getMetadata().getAnnotationAttributes(Scope.class.getName());
        if (attributes != null) {
            metadata.setScopeName((String) attributes.get("value"));
            ScopedProxyMode proxyMode = (ScopedProxyMode) attributes.get("proxyMode");
            if (proxyMode == null || proxyMode == ScopedProxyMode.DEFAULT) {
                proxyMode = ScopedProxyMode.NO;
            }
            metadata.setScopedProxyMode(proxyMode);
        }
    }

    boolean annotatedAsSingleton(AnnotationMetadata metadata) {
        return metadata.isAnnotated(Singleton.class.getName()) || metadata.isAnnotated(Configuration.class.getName());
    }
}

