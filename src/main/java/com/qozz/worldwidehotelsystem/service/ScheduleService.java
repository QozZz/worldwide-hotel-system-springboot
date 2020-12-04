package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.RentRoomDto;
import com.qozz.worldwidehotelsystem.data.dto.ScheduleDto;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.mapping.ScheduleMapper;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public List<ScheduleDto> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();

        return schedules.stream()
                .map(scheduleMapper::scheduleToScheduleDto)
                .collect(Collectors.toList());
    }

    public ScheduleDto createSchedule(RentRoomDto rentRoomDto, String email) {
        if (scheduleRepository.existsByRoomIdAndRentStartLessThanEqualAndRentEndGreaterThanEqual(
                rentRoomDto.getRoomId(),
                rentRoomDto.getRentEnd(),
                rentRoomDto.getRentStart())) {
            throw new RuntimeException("...");
        }

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("..."));

        Room room =  roomRepository.findById(rentRoomDto.getRoomId())
                .orElseThrow(() -> new RuntimeException("..."));

        Schedule schedule = new Schedule()
                .setRoom(room)
                .setUser(user)
                .setRentStart(rentRoomDto.getRentStart())
                .setRentEnd(rentRoomDto.getRentEnd());

        Schedule save = scheduleRepository.save(schedule);

        return scheduleMapper.scheduleToScheduleDto(save);
    }
}
