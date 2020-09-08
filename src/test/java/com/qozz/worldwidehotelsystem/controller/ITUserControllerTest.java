package com.qozz.worldwidehotelsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserDto;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ITUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAll() throws Exception {
        User save = userRepository.save(getUser());
        UserDto userDto = getUserDto().setId(save.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$[0].password", notNullValue()))
                .andExpect(jsonPath("$[0].roles", notNullValue()));
    }

    @Test
    void findById() throws Exception {
        User save = userRepository.save(getUser());
        UserDto userDto = getUserDto().setId(save.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + save.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.password", notNullValue()))
                .andExpect(jsonPath("$.roles", notNullValue()));
    }

    @Test
    void createUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getSignUpDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", is("test@test.com")))
                .andExpect(jsonPath("$.password", notNullValue()))
                .andExpect(jsonPath("$.roles", notNullValue()));
    }

    @Test
    void changeUser() throws Exception {
        User save = userRepository.save(getUser());

        UserDto userDto = UserDto.builder()
                .id(save.getId())
                .email("new@test.com")
                .password("newPass")
                .roles(Collections.asSet(Role.USER, Role.MODERATOR))
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is("new@test.com")))
                .andExpect(jsonPath("$.password", notNullValue()))
                .andExpect(jsonPath("$.roles", notNullValue()));
    }

    @Test
    void deleteUser() throws Exception {
        User save = userRepository.save(getUser());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/users/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<User> empty = userRepository.findById(save.getId());

        assertFalse(empty.isPresent());
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .password(passwordEncoder.encode("pass1"))
                .roles(Collections.asSet(Role.USER))
                .build();
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .id(1L)
                .email("test@test.com")
                .password(passwordEncoder.encode("pass1"))
                .roles(Collections.asSet(Role.USER))
                .build();
    }

    private SignUpDto getSignUpDto() {
        return SignUpDto.builder()
                .email("test@test.com")
                .password("pass1")
                .repeatPassword("pass1")
                .build();
    }
}