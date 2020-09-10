package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserDto;
import com.qozz.worldwidehotelsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sign-up")
public class SignUpController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return userService.createUser(signUpDto);
    }
}
