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
@Builder
public class ScheduleDto {

    private long id;
    private RoomDto roomDto;
    private String userEmail;
    private LocalDate rentStart;
    private LocalDate rentEnd;

}
