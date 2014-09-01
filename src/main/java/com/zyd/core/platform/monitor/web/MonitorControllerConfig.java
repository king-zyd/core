package com.zyd.core.platform.monitor.web;

import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
public class MonitorControllerConfig {
    @Bean
    public HealthCheckController healthCheckController() {
        return new HealthCheckController();
    }

    @Bean
    public MonitorController monitorController() {
        return new MonitorController();
    }

    @Bean
    public StatusController statusController() {
        return new StatusController();
    }

    @Bean
    public URLMappingController urlMappingController() {
        return new URLMappingController();
    }

    @Bean
    public ThreadInfoController threadInfoController() {
        return new ThreadInfoController();
    }

    @Bean
    public MemoryUsageController memoryUsageController() {
        return new MemoryUsageController();
    }

    @Bean
    public JobStatisticController jobStatisticController() {
        return new JobStatisticController();
    }

    @Bean
    public PerformanceController performanceController() {
        return new PerformanceController();
    }

    @Bean
    public ServiceInfoController serviceInfoController() {
        return new ServiceInfoController();
    }

    @Bean
    public RuntimeInfoController runtimeInfoController() {
        return new RuntimeInfoController();
    }


}
