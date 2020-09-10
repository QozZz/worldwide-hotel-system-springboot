package com.qozz.worldwidehotelsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozz.worldwidehotelsystem.config.security.JwtProvider;
import com.qozz.worldwidehotelsystem.data.dto.LoginDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ITLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void tokenIsNotEmpty() throws Exception {
        userRepository.save(getUser());

        LoginDto loginDto = getLoginDto();

        HttpServletResponse result = mockMvc
                .perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertFalse(Objects.requireNonNull(result.getHeader(JwtProvider.TOKEN_HEADER)).isEmpty());
    }

    @Test
    public void badCredentials() throws Exception {
        LoginDto wrongLoginDto = getLoginDto().setPassword("wrongPass");

        HttpServletResponse result = mockMvc
                .perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(wrongLoginDto))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();

        assertNull(result.getHeader(JwtProvider.TOKEN_HEADER));
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .password(passwordEncoder.encode("pass1"))
                .roles(Collections.asSet(Role.USER))
                .build();
    }

    private LoginDto getLoginDto() {
        return LoginDto.builder()
                .email("test@test.com")
                .password("pass1")
                .build();
    }
}