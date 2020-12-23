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

@AllArgsConstructor
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PreAuthorize("hasAnyRole('ROLE_USER' or 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleDto> findAll() {
        return scheduleService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER' or 'ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleDto createSchedule(@RequestBody RentRoomDto rentRoomDto,
                                      @AuthenticationPrincipal String currentUserEmail
    ) {
        return scheduleService.createSchedule(rentRoomDto, currentUserEmail);
    }

}
