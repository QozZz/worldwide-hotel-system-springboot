package com.qozz.worldwidehotelsystem.controller;

import com.google.common.collect.ImmutableList;
import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.User;
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
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final String USERS_ENDPOINT = "/admin/users/";

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "admin";
    private static final String USER_PASSWORD = "password";
    private static final Role USER_ROLE = Role.USER;
    private static final int EXPECTED_USER_ID = 1;

    private String jsonSignUp;
    private String jsonUser;
    private User user;
    private List<User> users;
    private List<UserInfoDto> usersInfo;
    private SignUpDto signUpDto;

    @InjectMocks
    private UserController controller;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws IOException {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = initUser();
        users = initUsers();
        usersInfo = initUsersInfo();
        jsonSignUp = readJsonWithFile("SignUpJSON.json");
        jsonUser = readJsonWithFile("UserJSON.json");
        signUpDto = initSignUpDto();
    }

    @Test
    public void getUsers() throws Exception {
        when(userService.getUserInfoList()).thenReturn(usersInfo);

        mockMvc.perform(get(USERS_ENDPOINT).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(EXPECTED_USER_ID)))
                .andExpect(jsonPath("$[0].username", is(USER_NAME)))
                .andExpect(jsonPath("$[0].roles[0]", is(USER_ROLE.name())));

        verify(userService).getUserInfoList();
    }

    @Test
    public void getUserById() throws Exception {
        when(userService.getUserById(USER_ID)).thenReturn(user);

        mockMvc.perform(get(USERS_ENDPOINT + USER_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_USER_ID)))
                .andExpect(jsonPath("$.username", is(USER_NAME)))
                .andExpect(jsonPath("$.password", is(USER_PASSWORD)))
                .andExpect(jsonPath("$.roles[0]", is(USER_ROLE.name())));

        verify(userService).getUserById(USER_ID);
    }

    @Test
    public void createUser() throws Exception {
        when(userService.createUser(signUpDto)).thenReturn(user);

        mockMvc.perform(post(USERS_ENDPOINT).contentType(APPLICATION_JSON).content(jsonSignUp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_USER_ID)))
                .andExpect(jsonPath("$.username", is(USER_NAME)))
                .andExpect(jsonPath("$.password", is(USER_PASSWORD)))
                .andExpect(jsonPath("$.roles[0]", is(USER_ROLE.name())));

        verify(userService).createUser(signUpDto);
    }

    @Test
    public void changeUser() throws Exception {
        when(userService.changeUser(user, USER_ID)).thenReturn(user);

        mockMvc.perform(put(USERS_ENDPOINT + USER_ID).contentType(APPLICATION_JSON).content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_USER_ID)))
                .andExpect(jsonPath("$.username", is(USER_NAME)))
                .andExpect(jsonPath("$.password", is(USER_PASSWORD)))
                .andExpect(jsonPath("$.roles[0]", is(USER_ROLE.name())));

        verify(userService).changeUser(user, USER_ID);
    }

    @Test
    public void deleteUser() throws Exception {
        doNothing().when(userService).deleteUserById(USER_ID);

        mockMvc.perform(delete(USERS_ENDPOINT + USER_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).deleteUserById(USER_ID);
    }

    private User initUser() {
        return new User()
                .setId(USER_ID)
                .setUsername(USER_NAME)
                .setPassword(USER_PASSWORD)
                .setRoles(Collections.singleton(USER_ROLE));
    }

    private List<User> initUsers() {
        return ImmutableList.of(
                new User()
                        .setId(USER_ID)
                        .setUsername(USER_NAME)
                        .setPassword(USER_PASSWORD)
                        .setRoles(Collections.singleton(USER_ROLE)));
    }

    private List<UserInfoDto> initUsersInfo() {
        return ImmutableList.of(
                new UserInfoDto()
                        .setId(USER_ID)
                        .setUsername(USER_NAME)
                        .setRoles(Collections.singleton(USER_ROLE)));
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
