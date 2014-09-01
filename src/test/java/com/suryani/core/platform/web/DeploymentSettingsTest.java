package com.zyd.core.platform.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author neo
 */
public class DeploymentSettingsTest {
    DeploymentSettings settings;
    MockServletContext servletContext;

    @Before
    public void createDeploymentSettings() {
        settings = new DeploymentSettings();
        servletContext = new MockServletContext();
    }

    @Test
    public void useRootAsDefaultDeploymentContext() {
        assertThat(settings.getDeploymentContext(), equalTo(""));
    }

    @Test
    public void useServletContextPathIfDeploymentContextNotSet() {
        servletContext.setContextPath("/context");
        settings.setDeploymentContext("", servletContext);

        assertThat(settings.getDeploymentContext(), equalTo("/context"));
    }

    @Test
    public void useDeploymentContextIfSet() {
        servletContext.setContextPath("/context");
        settings.setDeploymentContext("/", servletContext);

        assertThat(settings.getDeploymentContext(), equalTo("/"));
    }
}
