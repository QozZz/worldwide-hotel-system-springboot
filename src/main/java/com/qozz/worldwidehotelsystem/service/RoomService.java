package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.RoomDto;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.mapping.RoomMapper;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import com.qozz.worldwidehotelsystem.exception.EntityAlreadyExistsException;
import com.qozz.worldwidehotelsystem.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoomService {

    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;

    public List<RoomDto> findAllByHotelId(Long hotelId) {
        return roomRepository.findAllByHotelId(hotelId)
                .stream()
                .map(roomMapper::roomToRoomDto)
                .collect(Collectors.toList());
    }

    public RoomDto findById(Long id) {
        return roomRepository.findById(id)
                .map(roomMapper::roomToRoomDto)
                .orElseThrow(() -> new EntityNotFoundException("Room with Id [" + id + "] doesn't exist"));
    }

    public List<RoomDto> findAllAvailable(Long hotelId, LocalDate rentStart, LocalDate rentEnd) {
        List<Room> rooms = roomRepository.findAllAvailable(hotelId, rentStart, rentEnd);

        return rooms.stream()
                .map(roomMapper::roomToRoomDto)
                .collect(Collectors.toList());
    }

    public RoomDto createRoom(RoomDto roomDto) {
        if (roomRepository.existsByNumberAndHotelId(roomDto.getNumber(), roomDto.getHotelDto().getId())) {
            throw new EntityAlreadyExistsException("Hotel with: " +
                    "Number [" + roomDto.getNumber() + "] " +
                    "Hotel Id [" + roomDto.getHotelDto().getId() + "] already exists");
        }

        Room room = roomMapper.roomDtoToRoom(roomDto);

        Room save = roomRepository.save(room);

        return roomMapper.roomToRoomDto(save);
    }

    public RoomDto updateRoom(Long id, RoomDto roomDto) {
        if (roomRepository.existsByNumberAndHotelId(roomDto.getNumber(), roomDto.getHotelDto().getId())) {
            throw new EntityAlreadyExistsException("Hotel with: " +
                    "Number [" + roomDto.getNumber() + "] " +
                    "Hotel Id [" + roomDto.getHotelDto().getId() + "] already exists");
        }

        Room room = roomRepository.findById(id)
                .map(roomDb -> {
                    roomDb.setNumber(roomDto.getNumber());
                    roomDb.setPrice(roomDto.getPrice());
                    roomDb.setIsAvailable(roomDto.isAvailable());
                    return roomDb;
                })
                .orElseThrow(() -> new EntityNotFoundException("Room with Id [" + id + "] doesn't exist"));

        Room save = roomRepository.save(room);

        return roomMapper.roomToRoomDto(save);
    }

    public void deleteRoom(Long id) {
        try {
            roomRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Room with Id [" + id + "] doesn't exist");
        }
    }
}
