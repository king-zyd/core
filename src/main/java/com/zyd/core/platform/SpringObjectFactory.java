package com.zyd.core.platform;

import com.zyd.core.util.AssertUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
public class SpringObjectFactory {
    private ApplicationContext applicationContext;

    public <T> T createBean(Class<T> beanClass) {
        return applicationContext.getAutowireCapableBeanFactory().createBean(beanClass);
    }

    public <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public <T> List<T> getBeans(Class<T> beanClass) {
        return new ArrayList<>(applicationContext.getBeansOfType(beanClass).values());
    }

    @SuppressWarnings("unchecked")
    public <T> T initializeBean(T bean) {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(bean);
        return (T) beanFactory.initializeBean(bean, bean.getClass().getName());
    }

    public void registerSingletonBean(String beanName, Class beanClass) {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(beanClass);
        definition.setScope(BeanDefinition.SCOPE_SINGLETON);
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        registry.registerBeanDefinition(beanName, definition);
    }

    public void registerSingletonBean(String beanName, Object bean) {
        SingletonBeanRegistry registry = (SingletonBeanRegistry) applicationContext.getAutowireCapableBeanFactory();
        registry.registerSingleton(beanName, bean);
    }

    @Inject
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        AssertUtils.assertTrue(autowireCapableBeanFactory instanceof BeanDefinitionRegistry, "autowireCapableBeanFactory should be BeanDefinitionRegistry");
        AssertUtils.assertTrue(autowireCapableBeanFactory instanceof SingletonBeanRegistry, "autowireCapableBeanFactory should be BeanDefinitionRegistry");
    }
}
