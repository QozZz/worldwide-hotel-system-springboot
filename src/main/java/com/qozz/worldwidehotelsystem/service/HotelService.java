package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.HotelInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.mapping.HotelMapper;
import com.qozz.worldwidehotelsystem.data.repository.HotelRepository;
import com.qozz.worldwidehotelsystem.exception.HotelDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.qozz.worldwidehotelsystem.exception.ExceptionMessages.HOTEL_DOES_NOT_EXIST;

@Service
@AllArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public Hotel getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelDoesNotExistException(HOTEL_DOES_NOT_EXIST));
    }

    public List<HotelInfoDto> getHotelsInfo(String country, String city) {
        List<Hotel> hotels = hotelRepository.findAllHotelsByCountryAndCity(country, city);
        return hotelMapper.hotelsToHotelInfoDtoList(hotels);
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.saveAndFlush(hotel);
    }

    public Hotel changeHotel(Hotel newHotel, Long hotelId) {
        return hotelRepository.findById(hotelId)
                .map(hotel -> {
                    hotel.setName(newHotel.getName());
                    hotel.setStars(newHotel.getStars());
                    hotel.setCountry(newHotel.getCountry());
                    hotel.setCity(newHotel.getCity());
                    hotel.setStreet(newHotel.getStreet());
                    hotel.setNumber(newHotel.getNumber());
                    return hotelRepository.saveAndFlush(hotel);
                })
                .orElseThrow(() -> new HotelDoesNotExistException(HOTEL_DOES_NOT_EXIST));
    }

    @Transactional
    public void deleteHotel(Long hotelId) {
        hotelRepository.deleteHotelById(hotelId);
    }
}
