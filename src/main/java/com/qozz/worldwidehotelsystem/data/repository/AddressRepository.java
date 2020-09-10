package com.qozz.worldwidehotelsystem.data.repository;

import com.qozz.worldwidehotelsystem.data.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByCountryAndCityAndStreetAndNumber(String country, String city, String street, String number);

}
