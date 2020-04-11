package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.RentRoomDto;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.RoomAlreadyRentedException;
import com.qozz.worldwidehotelsystem.exception.RoomDoesNotExistException;
import com.qozz.worldwidehotelsystem.exception.UserDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.qozz.worldwidehotelsystem.exception.ExceptionMessages.*;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomDoesNotExistException(ROOM_DOES_NOT_EXIST));
    }

    public List<Room> getFreeRoomsInHotel(Long hotelId, LocalDate rentStart, LocalDate rentEnd) {
        return roomRepository.findAllFreeByHotelId(hotelId, rentStart, rentEnd);
    }

    public Schedule rentRoom(RentRoomDto rentRoomDto, String username) {
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
        return roomRepository.findNumberOfRentedRooms(
                rentRoomDto.getId(), rentRoomDto.getRentStart(), rentRoomDto.getRentEnd());
    }
}
