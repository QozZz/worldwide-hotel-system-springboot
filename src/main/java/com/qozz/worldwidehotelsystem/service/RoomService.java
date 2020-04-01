package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.RoomAlreadyRented;
import com.qozz.worldwidehotelsystem.exception.RoomDoesNotExist;
import com.qozz.worldwidehotelsystem.exception.UserDoesNotExist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {

    private final static String ROOM_DOES_NOT_EXIST = "Room does not exist!";
    public final static String USER_DOES_NOT_EXIST = "User does not exist!";
    public static final String ROOM_IS_ALREADY_RENTED = "Room is already rented!";

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public Optional<Room> findRoomById(Long roomId) {
        return roomRepository.findRoomById(roomId);
    }

    public List<Room> findFreeRoomsByHotelId(Long hotelId, LocalDate start, LocalDate end) {
        return roomRepository.findAllFreeByHotelId(hotelId, start, end);
    }

    public Schedule rentRoom(Long roomId, LocalDate start, LocalDate end, String username) {
        if (roomRepository.findNumberOfRentedRooms(roomId, start, end) != 0) {
            throw new RoomAlreadyRented(ROOM_IS_ALREADY_RENTED);
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDoesNotExist(USER_DOES_NOT_EXIST));
        Room room = findRoomById(roomId)
                .orElseThrow(() -> new RoomDoesNotExist(ROOM_DOES_NOT_EXIST));

        Schedule schedule = new Schedule()
                .setRoom(room)
                .setUser(user)
                .setRegisterStart(start)
                .setRegisterEnd(end);
        return scheduleRepository.save(schedule);
    }
}
