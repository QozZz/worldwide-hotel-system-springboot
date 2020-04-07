package com.qozz.worldwidehotelsystem.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignUpDto {

    private String username;
    private String password;
    private String repeatPassword;
}
