package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    void deleteHotelById(Long hotelId);

    @Query(value = "select * from hotel\n" +
            "where country like %?1% and city like %?2%", nativeQuery = true)
    List<Hotel> findAllHotelsByCountryAndCity(@Param("country") String country,
                                              @Param("city") String city);
}
