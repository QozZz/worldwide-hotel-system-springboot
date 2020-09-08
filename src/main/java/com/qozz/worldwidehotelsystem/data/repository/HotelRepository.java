package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

//    @Query("select h from Hotel h " +
//            "where h.address.country = ?1 and h.address.city = ?2 and h.address.street = ?3 and h.address.number = ?4")
    List<Hotel> findAllByAddressCountryAndAddressCityAndAddressStreetAndAddressNumber(
            @Param("country") String country,
            @Param("city") String city,
            @Param("street") String street,
            @Param("number") String number);

    boolean existsByName(String name);

}
