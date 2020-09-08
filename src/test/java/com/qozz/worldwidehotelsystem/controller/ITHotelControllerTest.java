package com.qozz.worldwidehotelsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozz.worldwidehotelsystem.data.dto.HotelDto;
import com.qozz.worldwidehotelsystem.data.entity.Address;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.mapping.HotelMapper;
import com.qozz.worldwidehotelsystem.data.repository.AddressRepository;
import com.qozz.worldwidehotelsystem.data.repository.HotelRepository;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ITHotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        scheduleRepository.deleteAll();
        roomRepository.deleteAll();
        hotelRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findAll() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("country", address.getCountry())
                        .param("city", address.getCity())
                        .param("street", address.getStreet())
                        .param("number", address.getNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(hotel.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(hotel.getName())))
                .andExpect(jsonPath("$[0].stars", is(hotel.getStars()), Integer.class))
                .andExpect(jsonPath("$[0].addressDto", notNullValue()));

    }

    @Test
    void findById() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hotels/" + hotel.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(hotel.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(hotel.getName())))
                .andExpect(jsonPath("$.stars", is(hotel.getStars()), Integer.class))
                .andExpect(jsonPath("$.addressDto", notNullValue()));
    }

    @Test
    void createHotel() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = getHotel().setAddress(address);

        HotelDto hotelDto = hotelMapper.hotelToHotelInfoDto(hotel);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(hotel.getName())))
                .andExpect(jsonPath("$.stars", is(hotel.getStars()), Integer.class))
                .andExpect(jsonPath("$.addressDto", notNullValue()));
    }

    @Test
    void updateHotel() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));

        HotelDto hotelDto = hotelMapper.hotelToHotelInfoDto(hotel);
        hotelDto.setName("newName")
                .setStars(3);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/hotels/" + hotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(hotel.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(hotelDto.getName())))
                .andExpect(jsonPath("$.stars", is(hotelDto.getStars()), Integer.class))
                .andExpect(jsonPath("$.addressDto", notNullValue()));
    }

    @Test
    void deleteHotel() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/hotels/" + hotel.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Room> empty = roomRepository.findById(hotel.getId());

        assertFalse(empty.isPresent());
    }

    private Hotel getHotel() {
        return Hotel.builder()
                .id(1L)
                .name("hotel X")
                .stars(5)
                .address(getAddress())
                .build();
    }

    private Address getAddress() {
        return Address.builder()
                .id(1L)
                .country("Country X")
                .city("City X")
                .street("Street X")
                .number("1")
                .build();
    }
}