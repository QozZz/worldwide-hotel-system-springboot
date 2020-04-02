package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hotels/{hotelId}/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<Room> getFreeHotelRooms(@PathVariable Long hotelId,
                                        @RequestParam(value = "start")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                        @RequestParam(value = "end")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return roomService.getFreeRoomsInHotel(hotelId, start, end);
    }

    @PostMapping(value = "/rent/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public Schedule rentRoom(@PathVariable Long roomId,
                             @RequestParam(value = "start")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                             @RequestParam(value = "end")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                             @AuthenticationPrincipal String username) {
        return roomService.rentRoom(roomId, start, end, username);
    }
}
