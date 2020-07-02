package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.RentRoomDto;
import com.qozz.worldwidehotelsystem.data.dto.RoomInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.mapping.RoomMapper;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.RoomAlreadyRentedException;
import com.qozz.worldwidehotelsystem.exception.RoomDoesNotExistException;
import com.qozz.worldwidehotelsystem.exception.UserDoesNotExistException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.qozz.worldwidehotelsystem.exception.ExceptionMessages.*;

@Service
@AllArgsConstructor
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final RoomMapper roomMapper;

    public Room getRoomById(Long roomId) {
        LOGGER.debug("getRoomById(): roomId = {}", roomId);
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomDoesNotExistException(ROOM_DOES_NOT_EXIST));
    }

    public List<RoomInfoDto> getFreeRoomsInHotel(Long hotelId, LocalDate rentStart, LocalDate rentEnd) {
        LOGGER.debug("getFreeRoomsInHotel(): hotelId = {}, rentStart = {}, rentEnd = {}", hotelId, rentStart, rentEnd);
        List<Room> allFreeRoomsByHotelId = roomRepository.findAllFreeByHotelId(hotelId, rentStart, rentEnd);
        return allFreeRoomsByHotelId.stream()
                .map(roomMapper::roomToRoomInfoDto)
                .collect(Collectors.toList());
    }

    public Schedule rentRoom(RentRoomDto rentRoomDto, String username) {
        LOGGER.debug("rentRoom(): rentRoomDto = {}, username = {}", rentRoomDto.toString(), username);
        if (getNumberOfRentedRooms(rentRoomDto) != 0) {
            throw new RoomAlreadyRentedException(ROOM_IS_ALREADY_RENTED);
        }

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserDoesNotExistException(USER_DOES_NOT_EXIST));
        Room room =  roomRepository.findById(rentRoomDto.getId())
                .orElseThrow(() -> new RoomDoesNotExistException(ROOM_DOES_NOT_EXIST));

        Schedule schedule = new Schedule()
                .setRoom(room)
                .setUser(user)
                .setRentStart(rentRoomDto.getRentStart())
                .setRentEnd(rentRoomDto.getRentEnd());

        return scheduleRepository.saveAndFlush(schedule);
    }

    private int getNumberOfRentedRooms(RentRoomDto rentRoomDto) {
        LOGGER.debug("getNumberOfRentedRooms(): rentRoomDto = {}", rentRoomDto.toString());
        return roomRepository.findNumberOfRentedRooms(
                rentRoomDto.getId(), rentRoomDto.getRentStart(), rentRoomDto.getRentEnd());
    }
}
