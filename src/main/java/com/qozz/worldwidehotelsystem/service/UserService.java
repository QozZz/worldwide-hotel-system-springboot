package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.config.security.JwtProvider;
import com.qozz.worldwidehotelsystem.data.dto.LoginDto;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.AuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private static final String MESSAGE_WRONG_PASSWORD = "Wrong password!";
    public static final String MESSAGE_WRONG_LOGIN = "Wrong username!";
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Transactional
    public String createUserToken(LoginDto loginDto) {
        String username = loginDto.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            try {
                return getAuthenticationToken(loginDto);
            } catch (BadCredentialsException e) {
                throw new AuthenticationException(MESSAGE_WRONG_PASSWORD);
            }
        } else {
            throw new AuthenticationException(MESSAGE_WRONG_LOGIN);
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
}
