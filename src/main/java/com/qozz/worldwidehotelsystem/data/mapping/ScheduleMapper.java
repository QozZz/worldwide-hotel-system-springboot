package com.qozz.worldwidehotelsystem.data.mapping;

import com.qozz.worldwidehotelsystem.data.dto.ScheduleDto;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ScheduleMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "roomDto.id", source = "room.id"),
            @Mapping(target = "roomDto.number", source = "room.number"),
            @Mapping(target = "roomDto.price", source = "room.price"),
            @Mapping(target = "roomDto.available", source = "room.isAvailable"),
            @Mapping(target = "roomDto.hotelDto.id", source = "room.hotel.id"),
            @Mapping(target = "roomDto.hotelDto.name", source = "room.hotel.name"),
            @Mapping(target = "roomDto.hotelDto.stars", source = "room.hotel.stars"),
            @Mapping(target = "roomDto.hotelDto.addressDto.id", source = "room.hotel.address.id"),
            @Mapping(target = "roomDto.hotelDto.addressDto.country", source = "room.hotel.address.country"),
            @Mapping(target = "roomDto.hotelDto.addressDto.city", source = "room.hotel.address.city"),
            @Mapping(target = "roomDto.hotelDto.addressDto.street", source = "room.hotel.address.street"),
            @Mapping(target = "roomDto.hotelDto.addressDto.number", source = "room.hotel.address.number"),
            @Mapping(target = "userEmail", source = "user.email"),
            @Mapping(target = "rentStart", source = "rentStart"),
            @Mapping(target = "rentEnd", source = "rentEnd"),

    })
    ScheduleDto scheduleToScheduleDto(Schedule schedule);

    @InheritInverseConfiguration
    Schedule scheduleDtoDtoToSchedule(ScheduleDto scheduleDto);

}
