package com.qozz.worldwidehotelsystem.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class RentRoomDto {

    private long id;
    private LocalDate rentStart;
    private LocalDate rentEnd;
}
