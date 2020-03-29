package com.qozz.worldwidehotelsystem.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 10)
    private String number;
}
