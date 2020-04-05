package com.qozz.worldwidehotelsystem.config.security;

import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserPrincipalDetailsServiceTest {

    private static final Long ID = 1L;
    private static final String USER_NAME = "test user";
    private static final String PASSWORD = "test password";
    private static final Role ROLE = Role.USER;

    private User user;

    @InjectMocks
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        user = initUser();
    }

    @Test
    public void successfulLoadUserByUsername() {
        when(userRepository.findUserByUsername(USER_NAME)).thenReturn(Optional.ofNullable(user));

        userPrincipalDetailsService.loadUserByUsername(USER_NAME);

        verify(userRepository).findUserByUsername(USER_NAME);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameWhenUserRepositoryReturnEmpty() {
        when(userRepository.findUserByUsername(USER_NAME)).thenReturn(Optional.empty());

        userPrincipalDetailsService.loadUserByUsername(USER_NAME);

        verify(userRepository).findUserByUsername(USER_NAME);
    }

    @Test
    public void assertLoadUserByUsername() {
        when(userRepository.findUserByUsername(USER_NAME)).thenReturn(Optional.ofNullable(user));

        UserDetails userDetails = userPrincipalDetailsService.loadUserByUsername(USER_NAME);

        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + ROLE)));
    }

    private User initUser() {
        return new User().setId(ID)
                .setUsername(USER_NAME)
                .setPassword(PASSWORD)
                .setRoles(Collections.singleton(ROLE));
    }
}
