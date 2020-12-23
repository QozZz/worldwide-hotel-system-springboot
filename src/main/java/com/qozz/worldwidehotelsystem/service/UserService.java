package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.config.security.JwtProvider;
import com.qozz.worldwidehotelsystem.data.dto.LoginDto;
import com.qozz.worldwidehotelsystem.data.dto.SignUpDto;
import com.qozz.worldwidehotelsystem.data.dto.UserDto;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.data.mapping.UserMapper;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.AuthException;
import com.qozz.worldwidehotelsystem.exception.EntityAlreadyExistsException;
import com.qozz.worldwidehotelsystem.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public String createUserToken(LoginDto loginDto) {
        String email = loginDto.getEmail();
        if (userRepository.findUserByEmail(email).isPresent()) {
            try {
                return getAuthenticationToken(loginDto);
            } catch (BadCredentialsException e) {
                throw new AuthException("Wrong Password");
            }
        } else {
            throw new AuthException("User [" + loginDto.getEmail() + "] doesn't exist");
        }
    }

    private String getAuthenticationToken(LoginDto loginDto) {
        String email = loginDto.getEmail();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginDto.getPassword()));

        Collection<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        return jwtProvider.createToken(email, roles);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userToUserDto)
                .orElseThrow(() -> new EntityNotFoundException("User with Id [" + id + "] doesn't exist"));
    }

    public UserDto createUser(SignUpDto signUpDto) {
        if (!signUpDto.getPassword().equals(signUpDto.getRepeatPassword())) {
            throw new AuthException("Passwords must match");
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new EntityAlreadyExistsException("User with email [" + signUpDto.getEmail() + "] already exist");
        }

        User user = new User()
                .setEmail(signUpDto.getEmail())
                .setPassword(passwordEncoder.encode(signUpDto.getPassword()))
                .setRoles(Collections.singleton(Role.USER));

        User save = userRepository.save(user);

        return userMapper.userToUserDto(save);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EntityAlreadyExistsException("User with email [" + userDto.getEmail() + "] already exists" +
                    "");
        }

        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(userDto.getEmail());
                    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    user.setRoles(userDto.getRoles());

                    User save = userRepository.save(user);

                    return userMapper.userToUserDto(save);
                })
                .orElseThrow(() -> new EntityNotFoundException("User with Id [" + userDto.getId() + "] doesn't exist"));
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User with Id [" + id + "] doesn't exist");
        }
    }
}
