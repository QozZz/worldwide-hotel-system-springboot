package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/sign-up")
@AllArgsConstructor
public class SignUpController {

    private final UserService userService;

    @PostMapping
    public UserInfoDto signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return userService.createUser(signUpDto);
    }
}
