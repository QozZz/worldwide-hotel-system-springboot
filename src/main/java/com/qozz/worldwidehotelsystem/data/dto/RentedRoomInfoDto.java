package com.qozz.worldwidehotelsystem.data.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class RentedRoomInfoDto {

    private String hotelName;
    private int number;
    private int price;
    private String country;
    private String city;
    private String street;
    private String streetNumber;
    private LocalDate rentStart;
    private LocalDate rentEnd;

}
