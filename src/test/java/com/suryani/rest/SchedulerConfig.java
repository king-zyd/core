package com.suryani.rest;

import com.zyd.core.platform.DefaultSchedulerConfig;
import com.zyd.core.platform.JobRegistry;
import org.springframework.context.annotation.Configuration;

/**
 * @author neo
 */
@Configuration
public class SchedulerConfig extends DefaultSchedulerConfig {
    @Override
    protected void configure(JobRegistry registry) {
    }
}
