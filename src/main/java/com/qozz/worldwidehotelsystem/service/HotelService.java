package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.HotelDto;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.mapping.HotelMapper;
import com.qozz.worldwidehotelsystem.data.repository.HotelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class HotelService {

    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;

    public List<HotelDto> findAll(String country, String city, String street, String number) {
        log.debug("getHotelsInfo(): country = {}, city = {}", country, city);

        return hotelRepository
                .findAllByAddressCountryAndAddressCityAndAddressStreetAndAddressNumber(country, city, street, number)
                .stream()
                .map(hotelMapper::hotelToHotelInfoDto)
                .collect(Collectors.toList());
    }

    public HotelDto findById(Long id) {
        log.debug("findById(): id = {}", id);

        return hotelRepository.findById(id)
                .map(hotelMapper::hotelToHotelInfoDto)
                .orElseThrow(() -> new RuntimeException("..."));
    }

    public HotelDto createHotel(HotelDto hotelDto) {
        log.debug("createHotel(): hotel = {}", hotelDto.toString());

        if (hotelRepository.existsByName(hotelDto.getName())) {
            throw new RuntimeException("...");
        }

        Hotel hotel = hotelMapper.hotelDtoToHotel(hotelDto);

        Hotel save = hotelRepository.save(hotel);

        return hotelMapper.hotelToHotelInfoDto(save);
    }

    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        log.debug("updateHotel(): hotelDto = {}, id = {}", hotelDto.toString(), id);

        if (hotelRepository.existsByName(hotelDto.getName())) {
            throw new RuntimeException("...");
        }

        Hotel hotel = hotelRepository.findById(id)
                .map(hotelDb -> {
                    hotelDb.setName(hotelDto.getName());
                    hotelDb.setStars(hotelDto.getStars());
                    return hotelDb;
                })
                .orElseThrow(() -> new RuntimeException("..."));

        Hotel save = hotelRepository.save(hotel);

        return hotelMapper.hotelToHotelInfoDto(save);
    }

    @Transactional
    public void deleteHotel(Long id) {
        log.debug("deleteHotel(): id = {}", id);

        hotelRepository.deleteById(id);
    }
}
