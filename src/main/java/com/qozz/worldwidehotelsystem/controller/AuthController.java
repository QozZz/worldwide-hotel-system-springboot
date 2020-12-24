package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.config.security.JwtProvider;
import com.qozz.worldwidehotelsystem.data.dto.LoginDto;
import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserDto;
import com.qozz.worldwidehotelsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        response.setHeader(JwtProvider.TOKEN_HEADER,
                JwtProvider.TOKEN_PREFIX + userService.createUserToken(loginDto));
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return userService.createUser(signUpDto);
    }

}
