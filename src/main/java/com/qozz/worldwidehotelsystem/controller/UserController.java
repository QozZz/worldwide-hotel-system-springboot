package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserInfoDto> getUsers() {
        return userService.getUserInfoList();
    }

    @GetMapping(value = "/{userId}")
    public UserInfoDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserInfoDto createUser(@RequestBody SignUpDto signUpDto) {
        return userService.createUser(signUpDto);
    }

    @PutMapping(value = "/{userId}")
    public UserInfoDto changeUser(@RequestBody UserInfoDto newUser, @PathVariable Long userId) {
        return userService.changeUser(newUser, userId);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
