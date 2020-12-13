package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "select r from Room r " +
            "inner join Schedule s on r.id = s.room.id " +
            "where r.hotel.id = ?1 " +
            "   and r.isAvailable = true " +
            "   and r.id not in " +
            "       (select s.room.id from Schedule s " +
            "           where s.rentStart <= ?3 and s.rentEnd >= ?2)" +
            "group by r.id ")
    List<Room> findAllAvailable(@Param("hotel_id") Long hotelId,
                                @Param("rent_start") LocalDate rentStart,
                                @Param("rent_end") LocalDate rentEnd);

    boolean existsByNumberAndHotelId(int number, long hotelId);

    List<Room> findAllByHotelId(Long hotelId);
}
