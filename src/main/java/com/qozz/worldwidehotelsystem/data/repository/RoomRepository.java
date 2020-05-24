package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "select r.id, r.floor, r.number, r.hotel_id, r.is_available, r.price\n" +
            "   from schedule as s\n" +
            "   join room as r on s.room_id = r.id\n" +
            "   join usr as u on s.usr_id = u.id\n" +
            "   where r.hotel_id = ?1 \n" +
            "       and r.is_available = true \n" +
            "       and r.id not in\n" +
            "       (select room_id from schedule\n" +
            "        where rent_start <= ?3 and rent_end >= ?2)\n" +
            "   group by r.id", nativeQuery = true)
    List<Room> findAllFreeByHotelId(@Param("hotel_id") Long hotelId,
                                    @Param("rent_start") LocalDate rentStart,
                                    @Param("rent_end") LocalDate rentEnd);

    @Query(value = "select count(*)\n" +
            "from schedule\n" +
            "where rent_start <= ?3 \n" +
            "and rent_end >= ?2 \n" +
            "and room_id = ?1", nativeQuery = true)
    int findNumberOfRentedRooms(@Param("room_id") Long roomId,
                                @Param("rent_start") LocalDate rentStart,
                                @Param("rent_end") LocalDate rentEnd);
}
