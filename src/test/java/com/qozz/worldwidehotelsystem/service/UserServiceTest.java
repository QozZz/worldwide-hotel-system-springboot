package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.config.security.JwtProvider;
import com.qozz.worldwidehotelsystem.data.dto.LoginDto;
import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.AuthenticationException;
import com.qozz.worldwidehotelsystem.exception.PasswordsAreNotEqualsException;
import com.qozz.worldwidehotelsystem.exception.UserAlreadyExistException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String WRONG_USER_NAME = "wrong username";
    private static final String USER_NAME = "test user";
    private static final String PASSWORD = "test password";
    private static final String TOKEN = "test token";
    private static final Long ID = 1L;

    private SignUpDto signUpDto;
    private LoginDto loginDto;
    private User user;


    @InjectMocks
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        signUpDto = initSignUpDto();
        loginDto = initLoginDTO();
        user = initUser();
    }

    @Test(expected = AuthenticationException.class)
    public void createUserTokenWhenUserDoesNotExist() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        userService.createUserToken(loginDto);

        verify(userRepository).findUserByUsername(WRONG_USER_NAME);
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtProvider, never()).createToken(anyString(), anyCollection());
    }

    @Test(expected = AuthenticationException.class)
    public void createUserTokensWhenUserExistsButWrongPassword() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Error"));

        userService.createUserToken(loginDto);

        verify(userRepository).findUserByUsername(loginDto.getUsername());
        verify(authenticationManager).authenticate(any());
        verify(jwtProvider, never()).createToken(anyString(), anyCollection());
    }

    @Test
    public void createUserTokensWhenUserExistsAndCorrectPassword() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtProvider.createToken(any(), anyCollection())).thenReturn(TOKEN);

        String userToken = userService.createUserToken(loginDto);

        verify(userRepository).findUserByUsername(USER_NAME);
        verify(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        verify(jwtProvider).createToken(USER_NAME, Collections.emptySet());

        assertEquals(TOKEN, userToken);
    }

    @Test(expected = PasswordsAreNotEqualsException.class)
    public void createUserWhenDifferentPasswords() {
        signUpDto.setRepeatPassword(PASSWORD + "different");

        userService.createUser(signUpDto);

        verify(userRepository, never()).findUserByUsername(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test(expected = UserAlreadyExistException.class)
    public void createUserWhenUserWithSameUserAlreadyRegistered() {
        when(userRepository.findUserByUsername(signUpDto.getUsername())).thenReturn(Optional.of(user));

        userService.createUser(signUpDto);

        verify(userRepository).findUserByUsername(signUpDto.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    public void createUserWhenCorrectPasswordsAndUserDoesNotExist() {
        User userToSave = user.setId(null).setRoles(Collections.singleton(Role.USER));

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(userToSave);

        User savedUser = userService.createUser(signUpDto);

        verify(userRepository).findUserByUsername(signUpDto.getUsername());
        verify(passwordEncoder).encode(signUpDto.getPassword());
        verify(userRepository).saveAndFlush(any(User.class));

        assertNotNull(savedUser);
    }

    private User initUser() {
        return new User()
                .setId(ID)
                .setUsername(USER_NAME)
                .setPassword(PASSWORD)
                .setRoles(Collections.singleton(Role.ADMIN));
    }

    private LoginDto initLoginDTO() {
        return new LoginDto()
                .setUsername(USER_NAME)
                .setPassword(PASSWORD);
    }

    private SignUpDto initSignUpDto() {
        return new SignUpDto()
                .setUsername(USER_NAME)
                .setPassword(PASSWORD)
                .setRepeatPassword(PASSWORD);
    }
}
