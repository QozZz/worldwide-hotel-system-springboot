package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.RentRoomDto;
import com.qozz.worldwidehotelsystem.data.dto.ScheduleDto;
import com.qozz.worldwidehotelsystem.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleDto> findAll() {
        return scheduleService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleDto createSchedule(@RequestBody RentRoomDto rentRoomDto,
                                      @AuthenticationPrincipal String email) {
        return scheduleService.createSchedule(rentRoomDto, email);
    }

}
