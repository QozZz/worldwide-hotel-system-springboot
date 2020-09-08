package com.qozz.worldwidehotelsystem.data.dto;

import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Builder
public class UserDto {
    
    private Long id;
    private String email;
    private String password;
    private Set<Role> roles;

}
