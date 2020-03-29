package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
