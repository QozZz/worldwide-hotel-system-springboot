package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findRoomById(Long roomId);

    @Query(value = "select r.id, r.floor, r.number, r.hotel_id\n" +
            "    from schedule as s\n" +
            "    join room r on s.room_id = r.id\n" +
            "    join usr u on s.usr_id = u.id\n" +
            "    where r.hotel_id = ?1 and r.id not in\n" +
            "    (select room_id from schedule\n" +
            "        where register_start <= ?3 and register_end >= ?2)\n" +
            "        group by r.id", nativeQuery = true)
    List<Room> findAllFreeByHotelId(@Param("hotel_id") Long hotelId,
                                    @Param("register_start") LocalDate start,
                                    @Param("register_end") LocalDate end);

    @Query(value = "select count(*)\n" +
            "from schedule\n" +
            "where register_start <= ?3 \n" +
            "and register_end >= ?2 \n" +
            "and room_id = ?1", nativeQuery = true)
    int findNumberOfRentedRooms(@Param("room_id") Long roomId,
                                @Param("register_start") LocalDate start,
                                @Param("register_end") LocalDate end);
}
