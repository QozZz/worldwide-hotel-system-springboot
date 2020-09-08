package com.qozz.worldwidehotelsystem.data.mapping;

import com.qozz.worldwidehotelsystem.data.dto.RoomDto;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RoomMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "number", source = "number"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "available", source = "isAvailable"),
            @Mapping(target = "hotelDto.id", source = "room.hotel.id"),
            @Mapping(target = "hotelDto.name", source = "room.hotel.name"),
            @Mapping(target = "hotelDto.stars", source = "room.hotel.stars"),
            @Mapping(target = "hotelDto.addressDto.id", source = "room.hotel.address.id"),
            @Mapping(target = "hotelDto.addressDto.country", source = "room.hotel.address.country"),
            @Mapping(target = "hotelDto.addressDto.city", source = "room.hotel.address.city"),
            @Mapping(target = "hotelDto.addressDto.street", source = "room.hotel.address.street"),
            @Mapping(target = "hotelDto.addressDto.number", source = "room.hotel.address.number"),
    })
    RoomDto roomToRoomDto(Room room);

    @InheritInverseConfiguration
    Room roomDtoToRoom(RoomDto roomDto);
}
