package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    boolean existsByRoomIdAndRentStartLessThanEqualAndRentEndGreaterThanEqual
            (Long roomId, LocalDate rentEnd, LocalDate rendStart);

}
