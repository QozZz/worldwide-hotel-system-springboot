package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.HotelInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.service.HotelService;
import com.qozz.worldwidehotelsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/hotels")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @GetMapping()
    public List<HotelInfoDto> getAllHotelsInfo(@RequestParam(required = false, defaultValue = "") String country,
                                               @RequestParam(required = false, defaultValue = "") String city) {
        return hotelService.getAllHotelsInfo(country, city);
    }

    @GetMapping(value = "/{hotelId}")
    public Hotel getHotel(@PathVariable Long hotelId) {
        return hotelService.getHotel(hotelId);
    }

    @GetMapping(value = "/{hotelId}/rooms")
    public List<Room> getFreeHotelRooms(@PathVariable Long hotelId,
                                        @RequestParam(value = "start")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                        @RequestParam(value = "end")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return roomService.findFreeRoomsByHotelId(hotelId, start, end);
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

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }

    @PutMapping(value = "/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Hotel changeHotel(@RequestBody Hotel hotel, @PathVariable Long hotelId) {
        return hotelService.changeHotel(hotel, hotelId);
    }

    @DeleteMapping(value = "/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteHotel(@PathVariable Long hotelId) {
        hotelService.deleteHotel(hotelId);
    }
}
