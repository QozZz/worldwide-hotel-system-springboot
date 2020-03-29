package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/hotels")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping(value = "/{hotelId}")
    public Hotel getHotel(@PathVariable Long hotelId) {
        return hotelService.getHotel(hotelId);
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
