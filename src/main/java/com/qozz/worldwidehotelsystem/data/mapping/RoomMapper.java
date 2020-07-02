package com.qozz.worldwidehotelsystem.data.mapping;

import com.qozz.worldwidehotelsystem.data.dto.RoomInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RoomMapper {
    @Mappings({
            @Mapping(source = "hotel.name", target = "hotelName"),
            @Mapping(source = "floor", target = "floor"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "hotel.country", target = "country"),
            @Mapping(source = "hotel.city", target = "city"),
            @Mapping(source = "hotel.street", target = "street"),
            @Mapping(source = "hotel.number", target = "streetNumber"),
    })
    RoomInfoDto roomToRoomInfoDto(Room room);
}
