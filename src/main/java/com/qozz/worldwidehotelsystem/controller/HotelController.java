package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.HotelDto;
import com.qozz.worldwidehotelsystem.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
//    public List<HotelDto> findAll(@RequestParam(required = false, defaultValue = "") String country,
//                                  @RequestParam(required = false, defaultValue = "") String city,
//                                  @RequestParam(required = false, defaultValue = "") String street,
//                                  @RequestParam(required = false, defaultValue = "") String number) {
//        return hotelService.findAll(country, city, street, number);
    public List<HotelDto> findAll() {
        return hotelService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HotelDto findById(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelDto createHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.createHotel(hotelDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public HotelDto updateHotel(@PathVariable Long id, @RequestBody HotelDto hotelDto) {
        return hotelService.updateHotel(id, hotelDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }
}
