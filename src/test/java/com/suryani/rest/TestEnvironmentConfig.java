package com.suryani.rest;

import com.zyd.core.http.HTTPClient;
import com.zyd.core.platform.scheduler.SchedulerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author neo
 */
@Profile("test")
@Configuration
public class TestEnvironmentConfig {

    @Bean
    public HTTPClient httpClient() {
        return new HTTPClient();
    }

    @Bean
    public SchedulerImpl scheduler() {
        return new MockScheduler();
    }
}
