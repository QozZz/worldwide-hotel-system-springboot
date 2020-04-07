package com.qozz.worldwidehotelsystem.exception;

import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;

public class PasswordsAreNotEqualsException extends RuntimeException {
    public PasswordsAreNotEqualsException(String message, SignUpDto signUpDto) {
        super(message + signUpDto.getPassword() + "/" + signUpDto.getRepeatPassword());
    }
}
