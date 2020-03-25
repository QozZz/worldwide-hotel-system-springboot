package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(value = "/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Can not find user with userId: " + userId));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping(value = "/{userId}")
    public User changeUser(@RequestBody User newUser, @PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(userId);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
    }
}
