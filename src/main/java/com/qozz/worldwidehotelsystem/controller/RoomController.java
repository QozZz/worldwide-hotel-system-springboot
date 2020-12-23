package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.RoomDto;
import com.qozz.worldwidehotelsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PreAuthorize("hasAnyRole('ROLE_USER' or 'ROLE_ADMIN')")
    @GetMapping("/hotel/{hotelId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomDto> findAllByHotelId(@PathVariable Long hotelId) {
        return roomService.findAllByHotelId(hotelId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER' or 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomDto findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER' or 'ROLE_ADMIN')")
    @GetMapping("/free")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomDto> findAllAvailable(@RequestParam(value = "hotelId") Long hotelId,
                                            @RequestParam(value = "rentStart")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentStart,
                                            @RequestParam(value = "rentEnd")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentEnd) {
        return roomService.findAllAvailable(hotelId, rentStart, rentEnd);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDto createRoom(@RequestBody RoomDto roomDto) {
        return roomService.createRoom(roomDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RoomDto updateRoom(@PathVariable Long id, @RequestBody RoomDto roomDto) {
        return roomService.updateRoom(id, roomDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}
