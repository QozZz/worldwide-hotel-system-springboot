package com.qozz.worldwidehotelsystem.data.mapping;

import com.qozz.worldwidehotelsystem.data.dto.HotelInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface HotelMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "address.country", target = "country"),
            @Mapping(source = "address.city", target = "city"),
            @Mapping(source = "address.street", target = "street"),
            @Mapping(source = "address.number", target = "number"),
    })
    HotelInfoDto hotelToHotelAddressDto(Hotel hotel);
}
