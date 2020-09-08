package com.qozz.worldwidehotelsystem.data.mapping;

import com.qozz.worldwidehotelsystem.data.dto.HotelDto;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface HotelMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "stars", source = "stars"),
            @Mapping(target = "addressDto.id", source = "hotel.address.id"),
            @Mapping(target = "addressDto.country", source = "hotel.address.country"),
            @Mapping(target = "addressDto.city", source = "hotel.address.city"),
            @Mapping(target = "addressDto.street", source = "hotel.address.street"),
            @Mapping(target = "addressDto.number", source = "hotel.address.number"),
    })
    HotelDto hotelToHotelInfoDto(Hotel hotel);

    @InheritInverseConfiguration
    Hotel hotelDtoToHotel(HotelDto hotelDto);
}
