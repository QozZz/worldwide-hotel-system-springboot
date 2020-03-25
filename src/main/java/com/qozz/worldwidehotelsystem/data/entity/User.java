package com.qozz.worldwidehotelsystem.data.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "usr")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;
}
