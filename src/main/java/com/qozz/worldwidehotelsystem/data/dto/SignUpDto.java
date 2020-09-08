package com.qozz.worldwidehotelsystem.data.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Builder
public class SignUpDto {

    public static final String REGEX_EMAIL = "^[\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String EMAIL_MESSAGE = "Email should be valid, abc@abc.abc";
    public static final String PASSWORD_REGEX = "^[\\w]{4,}$";
    public static final String PASSWORD_MESSAGE = "Allowed A-Z, a-z, 0-9, _";

    @Email(message = EMAIL_MESSAGE, regexp = REGEX_EMAIL)
    private String email;

    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String password;
    private String repeatPassword;

}
