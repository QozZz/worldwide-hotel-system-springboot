package com.qozz.worldwidehotelsystem.data.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class RoomDto {

    private long id;
    private int number;
    private int price;
    private boolean isAvailable;
    private HotelDto hotelDto;

}
