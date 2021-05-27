package com.zongshuo.web;

import com.alibaba.fastjson.JSONObject;
import com.zongshuo.model.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 17:56
 * @Description:
 */
@SpringBootTest
@WebAppConfiguration
class LoginControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        assertNotNull(webApplicationContext);
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        assertNotNull(mvc);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerUser() throws Exception {
        String url = "/api/sys/login/register";
        UserModel userModel = new UserModel();
        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(userModel)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    void sendCheckCode() {
    }
}