package com.zyd.core.platform.web;

import com.zyd.core.SpringServiceTest;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author neo
 */
public class RESTControllerAdviceTest extends SpringServiceTest {
    @Test
    public void methodNotAllowed() throws Exception {
        mockMvc.perform(get("/test/method-not-allowed"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(xpath("/error/message").string(JUnitMatchers.containsString("not supported")));
    }
}
