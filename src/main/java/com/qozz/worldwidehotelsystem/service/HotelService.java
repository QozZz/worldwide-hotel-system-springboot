package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.repository.HotelRepository;
import com.qozz.worldwidehotelsystem.exception.HotelDoesNotExist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HotelService {

    private final static String HOTEL_DOES_NOT_EXIST = "Hotel does not exist!";

    private final HotelRepository hotelRepository;

    public Hotel getHotel(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelDoesNotExist(HOTEL_DOES_NOT_EXIST));
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel changeHotel(Hotel newHotel, Long hotelId) {
        return hotelRepository.findById(hotelId)
                .map(hotel -> {
                    hotel.setName(newHotel.getName());
                    hotel.setAddress(newHotel.getAddress());
                    return hotelRepository.save(hotel);
                })
                .orElseGet(() -> {
                    newHotel.setId(hotelId);
                    return hotelRepository.save(newHotel);
                });
    }

    public void deleteHotel(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }
}
