package com.qozz.worldwidehotelsystem.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoomInfoDto {

    private String hotelName;
    private int floor;
    private int number;
    private int price;
    private String country;
    private String city;
    private String street;
    private String streetNumber;
}
