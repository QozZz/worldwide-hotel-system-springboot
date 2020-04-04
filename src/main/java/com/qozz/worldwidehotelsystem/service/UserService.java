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
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private static final String MESSAGE_WRONG_PASSWORD = "Wrong password!";
    private static final String MESSAGE_WRONG_USER_NAME = "Wrong username!";
    private static final String PASSWORDS_ARE_NOT_EQUALS = "passwords are not equals! ";
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String createUserToken(LoginDto loginDto) {
        String username = loginDto.getUsername();
        if (userRepository.findUserByUsername(username).isPresent()) {
            try {
                return getAuthenticationToken(loginDto);
            } catch (BadCredentialsException e) {
                throw new AuthenticationException(MESSAGE_WRONG_PASSWORD);
            }
        } else {
            throw new AuthenticationException(MESSAGE_WRONG_USER_NAME);
        }
    }

    private String getAuthenticationToken(LoginDto loginDto) {
        String username = loginDto.getUsername();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));
        Collection<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return jwtProvider.createToken(username, roles);
    }

    public User createUser(SignUpDto signUpDto) {
        if (!signUpDto.getPassword().equals(signUpDto.getRepeatPassword())) {
            throw new PasswordsAreNotEqualsException(PASSWORDS_ARE_NOT_EQUALS, signUpDto);
        }

        Optional<User> newUser = userRepository.findUserByUsername(signUpDto.getUsername());

        if (newUser.isPresent()) {
            throw new UserAlreadyExistException("User with username (" + signUpDto.getUsername() + ") already exist!");
        }

        User user = new User()
                .setUsername(signUpDto.getUsername())
                .setPassword(passwordEncoder.encode(signUpDto.getPassword()))
                .setRoles(Collections.singleton(Role.USER));

        return userRepository.saveAndFlush(user);
    }
}
