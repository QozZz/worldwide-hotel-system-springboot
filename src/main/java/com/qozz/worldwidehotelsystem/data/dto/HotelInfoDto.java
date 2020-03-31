package com.qozz.worldwidehotelsystem.data.dto;

import lombok.Data;

@Data
public class HotelInfoDto {

    private long id;
    private String name;
    private String country;
    private String city;
    private String street;
    private String number;
}
