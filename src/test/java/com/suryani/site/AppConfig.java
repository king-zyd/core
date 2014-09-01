package com.suryani.site;

import com.zyd.core.platform.DefaultAppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * @author neo
 */
@Configuration
public class AppConfig extends DefaultAppConfig {

    @Inject
    Environment env;

}
