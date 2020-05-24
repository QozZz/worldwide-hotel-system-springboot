package com.qozz.worldwidehotelsystem.data.mapping;

import com.qozz.worldwidehotelsystem.data.dto.RoomInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RoomMapper {
    @Mappings({
            @Mapping(source = "room.hotel.name", target = "hotelName"),
            @Mapping(source = "room.floor", target = "floor"),
            @Mapping(source = "room.number", target = "number"),
            @Mapping(source = "room.price", target = "price"),
            @Mapping(source = "room.hotel.country", target = "country"),
            @Mapping(source = "room.hotel.city", target = "city"),
            @Mapping(source = "room.hotel.street", target = "street"),
            @Mapping(source = "room.hotel.number", target = "streetNumber"),
    })
    RoomInfoDto scheduleToRoomInfoDto(Schedule schedule);
}
