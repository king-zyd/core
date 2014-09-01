package com.zyd.core.platform.management.web;

import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
public class ManagementControllerConfig {
    @Bean
    public LogManagementController logManagementController() {
        return new LogManagementController();
    }

    @Bean
    public CacheManagementController cacheManagementController() {
        return new CacheManagementController();
    }

    @Bean
    public ServerStatusManagementController serverStatusManagementController() {
        return new ServerStatusManagementController();
    }

    @Bean
    public SchedulerManagementController schedulerManagementController() {
        return new SchedulerManagementController();
    }
}
