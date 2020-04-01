package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> findFreeRoomsByHotelId(Long hotelId, LocalDate start, LocalDate end) {
        return roomRepository.findAllFreeByHotelId(hotelId, start, end);
    }
}
