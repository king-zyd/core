package com.zyd.core.platform.monitor.exception;

import com.zyd.core.SpringSiteTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Chi
 */
public class ExceptionMonitorControllerTest extends SpringSiteTest {
    @Inject
    ExceptionMonitor exceptionMonitor;

    @Test
    public void countWarningInJSON() throws Exception {
        exceptionMonitor.warnings = 1;

        mockMvc.perform(get("/monitor/exceptions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warnings").value(1));
    }

    @Test
    public void countErrorInXML() throws Exception {
        exceptionMonitor.errors = 1;

        mockMvc.perform(get("/monitor/exceptions").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(xpath("/exceptions/errors").number(1d));

    }

    @Test
    public void monitorNames() throws Exception {
        mockMvc.perform(get("/monitors").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }
}
