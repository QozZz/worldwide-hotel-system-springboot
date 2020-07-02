package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserInfoDto;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.service.UserService;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class SignUpControllerTest {

    private static final String SIGN_UP_ENDPOINT = "/sign-up/";

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "admin";
    private static final String USER_PASSWORD = "password";
    private static final Role USER_ROLE = Role.USER;
    private static final int EXPECTED_USER_ID = 1;

    private String jsonSignUp;
    private SignUpDto signUpDto;
    private UserInfoDto userInfoDto;

    @InjectMocks
    private SignUpController controller;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws IOException {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        jsonSignUp = readJsonWithFile("json/SignUpJSON.json");
        signUpDto = initSignUpDto();
        userInfoDto = initUserInfoDto();
    }

    @Test
    public void SignUpUser() throws Exception {
        when(userService.createUser(signUpDto)).thenReturn(userInfoDto);

        mockMvc.perform(post(SIGN_UP_ENDPOINT).contentType(APPLICATION_JSON).content(jsonSignUp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_USER_ID)))
                .andExpect(jsonPath("$.username", is(USER_NAME)))
                .andExpect(jsonPath("$.password", is(USER_PASSWORD)))
                .andExpect(jsonPath("$.roles[0]", is(USER_ROLE.name())));

        verify(userService).createUser(signUpDto);
    }

    private UserInfoDto initUserInfoDto() {
        return new UserInfoDto()
                .setId(USER_ID)
                .setUsername(USER_NAME)
                .setPassword(USER_PASSWORD)
                .setRoles(Collections.singleton(USER_ROLE));
    }

    private SignUpDto initSignUpDto() {
        return new SignUpDto()
                .setUsername("admin")
                .setPassword("password")
                .setRepeatPassword("password");
    }

    private String readJsonWithFile(String jsonFile) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFile);
        assert inputStream != null;
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }
}
