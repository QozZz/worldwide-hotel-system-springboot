package com.qozz.worldwidehotelsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/", "/home"})
public class HomeController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getHome() {
        return "Hello from HomeController!";
    }
}
