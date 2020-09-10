package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

}
