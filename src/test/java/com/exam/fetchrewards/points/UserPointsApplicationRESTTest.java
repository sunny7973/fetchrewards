package com.exam.fetchrewards.points;

import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class UserPointsApplicationRESTTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    public void createPoint() throws Exception {
        String json = "{\"payer_name\": \"DANNON\", \"points\": 300, \"time\": \"02/12 10AM \"}";

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());

        json = "{\"payer_name\": \"UNILEVER\", \"points\": 200, \"time\": \"02/11 11AM \"}";

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());

        json = "{\"payer_name\": \"DANNON\", \"points\": -200, \"time\": \"02/11 3PM \"}";

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());

        json = "{\"payer_name\": \"MILLER COORS\", \"points\": 10000, \"time\": \"02/12 2PM \"}";

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());

        json = "{\"payer_name\": \"DANNON\", \"points\": 1000, \"time\": \"02/13 2PM \"}";

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());

    }

    @Test
    @Order(2)
    public void deductPoint() throws Exception {

        mockMvc.perform(
                get("/user/deduct/5000")
        ).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void compareResponse() throws Exception {

        String res = "{\"payer_name\": \"MILLER COORS\", \"points\": 10000, \"time\": \"02/12 2PM \"}";

        JSONAssert.assertEquals(res, mockMvc.perform(get("/user/deduct/5000"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), false);
    }

}
