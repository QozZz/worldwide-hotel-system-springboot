package com.qozz.worldwidehotelsystem.config.security;

import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserPrincipalDetailsServiceTest {

    private static final Long ID = 1L;
    private static final String USER_EMAIL = "test@test.com";
    private static final String PASSWORD = "test password";
    private static final Role ROLE = Role.USER;

    private User user;

    @InjectMocks
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        user = getUser();
    }

    @Test
    public void successfulLoadUserByUsername() {
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(user));

        userPrincipalDetailsService.loadUserByUsername(USER_EMAIL);

        verify(userRepository).findUserByEmail(USER_EMAIL);
    }

    @Test
    public void loadUserByUsernameWhenUserRepositoryReturnEmpty() {
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                userPrincipalDetailsService.loadUserByUsername(USER_EMAIL));

        verify(userRepository).findUserByEmail(USER_EMAIL);
    }

    @Test
    public void assertLoadUserByUsername() {
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(user));

        UserDetails userDetails = userPrincipalDetailsService.loadUserByUsername(USER_EMAIL);

        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + ROLE)));
    }

    private User getUser() {
        return new User()
                .setId(ID)
                .setEmail(USER_EMAIL)
                .setPassword(PASSWORD)
                .setRoles(Collections.singleton(ROLE));
    }
}
