package com.zyd.core.platform;

import com.zyd.core.log.ActionLoggerImpl;
import com.zyd.core.log.LogSettings;
import com.zyd.core.platform.context.Messages;
import com.zyd.core.platform.exception.ErrorHandler;
import com.zyd.core.platform.monitor.MonitorManager;
import com.zyd.core.platform.monitor.ServerStatus;
import com.zyd.core.platform.monitor.exception.ExceptionMonitor;
import com.zyd.core.platform.monitor.exception.RecentExceptions;
import com.zyd.core.platform.monitor.performance.PerformanceStatistics;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.scheduler.SchedulerImpl;
import com.zyd.core.platform.scheduler.info.JobStatistic;
import com.zyd.core.platform.service.ManagedServiceApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * @author neo
 */
public class DefaultAppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:*.properties"));
        return propertySourcesPlaceholderConfigurer;
    }

    @Bean
    Messages messages() throws IOException {
        Resource[] messageResources = new PathMatchingResourcePatternResolver().getResources("classpath*:messages/*.properties");
        Messages messages = new Messages();
        String[] baseNames = new String[messageResources.length];
        for (int i = 0, messageResourcesLength = messageResources.length; i < messageResourcesLength; i++) {
            Resource messageResource = messageResources[i];
            String filename = messageResource.getFilename();
            baseNames[i] = "messages/" + filename.substring(0, filename.indexOf('.'));
        }
        messages.setBasenames(baseNames);
        return messages;
    }

    @Bean
    ManagedServiceApplicationListener managedServiceApplicationListener() {
        return new ManagedServiceApplicationListener();
    }

    @Bean
    SpringObjectFactory springObjectFactory() {
        return new SpringObjectFactory();
    }

    @Bean
    ActionLoggerImpl actionLogger() {
        return ActionLoggerImpl.get();
    }

    @Bean
    ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    public ExceptionMonitor exceptionMonitor() {
        return new ExceptionMonitor();
    }

    @Bean
    public RecentExceptions recentExceptions() {
        return new RecentExceptions();
    }

    @Bean
    MonitorManager monitorManager() {
        return new MonitorManager();
    }

    @Bean
    public RuntimeSettings runtimeSettings() {
        return RuntimeSettings.get();
    }

    @Bean
    public LogSettings logSettings() {
        return LogSettings.get();
    }

    @Bean
    SchedulerImpl scheduler() {
        return new SchedulerImpl();
    }

    @Bean
    ServerStatus serverStatus() {
        return new ServerStatus();
    }

    @Bean
    JobStatistic jobStatistic() {
        return new JobStatistic();
    }

    @Bean
    PerformanceStatistics performanceStatistics() {
        return new PerformanceStatistics();
    }
}
