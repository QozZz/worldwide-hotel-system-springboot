package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.config.security.JwtProvider;
import com.qozz.worldwidehotelsystem.data.dto.LoginDto;
import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.data.mapping.UserMapper;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.AuthenticationException;
import com.qozz.worldwidehotelsystem.exception.PasswordsAreNotEqualsException;
import com.qozz.worldwidehotelsystem.exception.UserAlreadyExistException;
import com.qozz.worldwidehotelsystem.exception.UserDoesNotExistException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.qozz.worldwidehotelsystem.exception.ExceptionMessages.*;

@Service
@AllArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public String createUserToken(LoginDto loginDto) {
        LOGGER.debug("createUserToken(): loginDto = {}", loginDto.toString());
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
        LOGGER.debug("getAuthenticationToken(): loginDto = {}", loginDto.toString());
        String username = loginDto.getUsername();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));
        Collection<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return jwtProvider.createToken(username, roles);
    }

    public UserInfoDto getUserById(Long userId) {
        LOGGER.debug("getUserById(): userId = {}", userId);
        return userRepository.findById(userId)
                .map(userMapper::userToUserInfoDto)
                .orElseThrow(() -> new UserDoesNotExistException(USER_DOES_NOT_EXIST));
    }

    public List<UserInfoDto> getUserInfoList() {
        LOGGER.debug("getUserInfoList()");
        List<User> users = userRepository.findAll();
        return userMapper.userListToUserIntoDtoList(users);
    }

    public UserInfoDto createUser(SignUpDto signUpDto) {
        LOGGER.debug("createUser(): signUpDto = {}", signUpDto.toString());
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

        User saveUser = userRepository.saveAndFlush(user);

        return userMapper.userToUserInfoDto(saveUser);
    }

    public UserInfoDto changeUser(UserInfoDto newUser, Long userId) {
        LOGGER.debug("changeUser(): newUser = {}, userId = {}", newUser, userId);
        return userRepository.findById(userId)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    User saveUser = userRepository.saveAndFlush(user);
                    return userMapper.userToUserInfoDto(saveUser);
                })
                .orElseThrow(() -> new UserDoesNotExistException(USER_DOES_NOT_EXIST));
    }

    public void deleteUserById(Long userId) {
        LOGGER.debug("deleteUserById(): userId = {}", userId);
        userRepository.deleteById(userId);
    }
}
