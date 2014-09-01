package com.zyd.core.platform.web;

import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;

/**
 * @author neo
 */
public class ControllerHelper {
    /**
     * find whether one annotation is marked on method or class level included parent class,
     * this pattern is used to check whether one flag is declared on controller by interceptors
     *
     * @param handler            the handler
     * @param annotationClass<A> annotation class
     * @return annotation found, or null if not found
     */
    public static <A extends Annotation> A findMethodOrClassLevelAnnotation(Object handler, Class<A> annotationClass) {
        if (!(handler instanceof HandlerMethod)) return null;
        A annotationOnMethod = ((HandlerMethod) handler).getMethodAnnotation(annotationClass);
        if (annotationOnMethod != null) return annotationOnMethod;
        Class<?> targetClass = ((HandlerMethod) handler).getBeanType();
        while (true) {
            A annotation = targetClass.getAnnotation(annotationClass);
            if (annotation != null) return annotation;
            targetClass = targetClass.getSuperclass();
            if (Object.class.equals(targetClass)) return null;
        }
    }
}
