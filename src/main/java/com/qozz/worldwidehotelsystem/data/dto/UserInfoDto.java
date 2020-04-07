package com.qozz.worldwidehotelsystem.data.dto;

import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class UserInfoDto {
    
    private Long id;
    private String username;
    private Set<Role> roles;
}
