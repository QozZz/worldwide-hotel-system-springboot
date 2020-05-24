package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.config.security.JwtProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    private String loginDto;
    private String wrongLoginDto;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        loginDto = "{\"username\":\"admin\",\"password\":\"p\"}";
        wrongLoginDto = "{\"username\":\"admin\",\"password\":\"Wrong Password\"}";
    }

    @Test
    public void tokenIsNotEmpty() throws Exception {
        HttpServletResponse result = mockMvc
                .perform(post("/login").content(loginDto)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertFalse(Objects.requireNonNull(result.getHeader(JwtProvider.TOKEN_HEADER)).isEmpty());
    }

    @Test
    public void badCredentials() throws Exception {
        HttpServletResponse result = mockMvc
                .perform(post("/login").content(wrongLoginDto).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertNull(result.getHeader(JwtProvider.TOKEN_HEADER));
    }
}
