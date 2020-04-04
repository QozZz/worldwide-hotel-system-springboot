package com.qozz.worldwidehotelsystem.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HotelInfoDto {

    private long id;
    private String name;
    private int stars;
    private String country;
    private String city;
    private String street;
    private String number;
}
