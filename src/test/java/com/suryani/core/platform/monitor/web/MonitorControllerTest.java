package com.zyd.core.platform.monitor.web;

import com.zyd.core.SpringSiteTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Chi
 */
public class MonitorControllerTest extends SpringSiteTest {
    @Test
    public void monitorNotFound() throws Exception {
        mockMvc.perform(get("/monitor/not-exists"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void allMonitors() throws Exception {
        mockMvc.perform(get("/monitors").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }
}
