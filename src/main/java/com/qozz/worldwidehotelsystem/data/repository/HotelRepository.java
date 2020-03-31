package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query(value = "select h.id, h.name, h.address_id " +
            "from hotel as h join address as a on a.id = h.address_id " +
            "where a.country like %?1% and a.city like %?2%", nativeQuery = true)
    List<Hotel> findAllHotelsByCountryAndCity(@Param("country") String country,
                                              @Param("city") String city);
}
