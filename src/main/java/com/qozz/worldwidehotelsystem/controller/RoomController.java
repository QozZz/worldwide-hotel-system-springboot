package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.RentRoomDto;
import com.qozz.worldwidehotelsystem.data.dto.RentedRoomInfoDto;
import com.qozz.worldwidehotelsystem.data.dto.RoomInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.data.mapping.ScheduleMapper;
import com.qozz.worldwidehotelsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final ScheduleMapper scheduleMapper;

    @GetMapping(value = "/{roomId}")
    public Room getRoomById(@PathVariable Long roomId) {
        return roomService.getRoomById(roomId);
    }

    @GetMapping
    public List<RoomInfoDto> getFreeHotelRooms(@RequestParam(value = "hotelId") Long hotelId,
                                        @RequestParam(value = "start")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentStart,
                                        @RequestParam(value = "end")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentEnd) {
        return roomService.getFreeRoomsInHotel(hotelId, rentStart, rentEnd);
    }

    @PostMapping(value = "/rent")
    @PreAuthorize("isAuthenticated()")
    public RentedRoomInfoDto rentRoom(@RequestBody RentRoomDto rentRoomDto,
                                      @AuthenticationPrincipal String username) {
        Schedule schedule = roomService.rentRoom(rentRoomDto, username);
        return scheduleMapper.scheduleToRentInfoDto(schedule);
    }
}
