package com.qozz.worldwidehotelsystem.service;

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

@Service
@AllArgsConstructor
public class RoomService {

    private final static String ROOM_DOES_NOT_EXIST = "Room does not exist!";
    public final static String USER_DOES_NOT_EXIST = "User does not exist!";
    public static final String ROOM_IS_ALREADY_RENTED = "Room is already rented!";

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public Room getRoomById(Long roomId) {
        return roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new RoomDoesNotExistException(ROOM_DOES_NOT_EXIST));
    }

    public List<Room> getFreeRoomsInHotel(Long hotelId, LocalDate start, LocalDate end) {
        return roomRepository.findAllFreeByHotelId(hotelId, start, end);
    }

    public Schedule rentRoom(Long roomId, LocalDate start, LocalDate end, String username) {
        if (roomRepository.findNumberOfRentedRooms(roomId, start, end) != 0) {
            throw new RoomAlreadyRentedException(ROOM_IS_ALREADY_RENTED);
        }

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserDoesNotExistException(USER_DOES_NOT_EXIST));
        Room room =  roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new RoomDoesNotExistException(ROOM_DOES_NOT_EXIST));

        Schedule schedule = new Schedule()
                .setRoom(room)
                .setUser(user)
                .setRegisterStart(start)
                .setRegisterEnd(end);

        return scheduleRepository.saveAndFlush(schedule);
    }
}
