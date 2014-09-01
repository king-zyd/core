package com.suryani.site;

import com.zyd.core.SpringSiteTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * User: Lance.zhou
 * Date: 12/13/13
 */
public class DateFormatTest extends SpringSiteTest {

    @Test
    public void simpleArguments() throws Exception {
        mockMvc.perform(get("/test/date-simple")
                .param("date", "12/23/2013"))
                .andExpect(content().string("2013-12-23T00:00:00"));
    }

    @Test
    public void formArguments() throws Exception {
        mockMvc.perform(post("/test/date-form")
                .content("")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("date", "12/23/2013"))
                .andExpect(content().string("2013-12-23T00:00:00"));
    }

    @Test
    public void jsonArguments() throws Exception {
        mockMvc.perform(post("/test/date-json")
                .content("{\"date\":\"2013-12-23T00:00:00\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"date\":\"2013-12-23T00:00:00\"}"));
    }

}
