package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.HotelInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.mapping.HotelMapper;
import com.qozz.worldwidehotelsystem.data.repository.HotelRepository;
import com.qozz.worldwidehotelsystem.exception.HotelDoesNotExist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class HotelService {

    private final static String HOTEL_DOES_NOT_EXIST = "Hotel does not exist!";

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public Hotel getHotelById(Long hotelId) {
        return hotelRepository.findHotelById(hotelId)
                .orElseThrow(() -> new HotelDoesNotExist(HOTEL_DOES_NOT_EXIST));
    }

    public List<HotelInfoDto> getHotelsInfo(String country, String city) {
        List<Hotel> hotels = hotelRepository.findAllHotelsByCountryAndCity(country, city);
        return hotelMapper.hotelsToHotelInfoDtoList(hotels);
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.saveAndFlush(hotel);
    }

    public Hotel changeHotel(Hotel newHotel, Long hotelId) {
        return hotelRepository.findHotelById(hotelId)
                .map(hotel -> {
                    hotel.setName(newHotel.getName());
                    hotel.setStars(newHotel.getStars());
                    hotel.setCountry(newHotel.getCountry());
                    hotel.setCity(newHotel.getCity());
                    hotel.setStreet(newHotel.getStreet());
                    hotel.setNumber(newHotel.getNumber());
                    return hotelRepository.saveAndFlush(hotel);
                })
                .orElseThrow(() -> new HotelDoesNotExist(HOTEL_DOES_NOT_EXIST));
    }

    @Transactional
    public void deleteHotel(Long hotelId) {
        hotelRepository.deleteHotelById(hotelId);
    }
}
