package com.zyd.core.platform.web.site.handler;

import com.zyd.core.SpringSiteTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

/**
 * @author neo
 */
public class ResourceNotFoundHandlerMappingTest extends SpringSiteTest {
    // refer to WebConfig
    @Test
    public void forwardToResourceNotFoundPage() throws Exception {
        mockMvc.perform(get("/test/not-existed-uri"))
                .andExpect(forwardedUrl("/error/resource-not-found"));
    }
}
