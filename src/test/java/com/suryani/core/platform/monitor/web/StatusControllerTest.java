package com.zyd.core.platform.monitor.web;

import com.zyd.core.SpringServiceTest;
import com.zyd.core.platform.monitor.ServiceStatus;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author neo
 */
public class StatusControllerTest extends SpringServiceTest {
    @Test
    public void getStatus() throws Exception {
        mockMvc.perform(get("/monitor/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(xpath("/status/server").string(ServiceStatus.UP.name()));
    }
}
