package com.qozz.worldwidehotelsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozz.worldwidehotelsystem.data.dto.RoomDto;
import com.qozz.worldwidehotelsystem.data.entity.Address;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.mapping.RoomMapper;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ITRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomMapper roomMapper;

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
        Room room = roomRepository.save(getRoom().setHotel(hotel));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(room.getId()), Long.class))
                .andExpect(jsonPath("$[0].number", is(room.getNumber()), Integer.class))
                .andExpect(jsonPath("$[0].price", is(room.getPrice()), Integer.class))
                .andExpect(jsonPath("$[0].available", is(room.getIsAvailable())))
                .andExpect(jsonPath("$[0].hotelDto", notNullValue()));
    }

    @Test
    void findById() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));
        Room room = roomRepository.save(getRoom().setHotel(hotel));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/rooms/" + room.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(room.getId()), Long.class))
                .andExpect(jsonPath("$.number", is(room.getNumber()), Integer.class))
                .andExpect(jsonPath("$.price", is(room.getPrice()), Integer.class))
                .andExpect(jsonPath("$.available", is(room.getIsAvailable())))
                .andExpect(jsonPath("$.hotelDto", notNullValue()));
    }

    @Test
    void findAllAvailable() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));
        Room room = roomRepository.save(getRoom().setHotel(hotel));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("hotelId", String.valueOf(room.getId()))
                        .param("rentStart", String.valueOf(LocalDate.of(2020, 9, 27)))
                        .param("rentEnd", String.valueOf(LocalDate.of(2020, 9, 29))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(room.getId()), Long.class))
                .andExpect(jsonPath("$[0].number", is(room.getNumber()), Integer.class))
                .andExpect(jsonPath("$[0].price", is(room.getPrice()), Integer.class))
                .andExpect(jsonPath("$[0].available", is(room.getIsAvailable())))
                .andExpect(jsonPath("$[0].hotelDto", notNullValue()));
    }

    @Test
    void createRoom() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));
        Room room = getRoom().setHotel(hotel);

        RoomDto roomDto = roomMapper.roomToRoomDto(room);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber()), Integer.class))
                .andExpect(jsonPath("$.price", is(roomDto.getPrice()), Integer.class))
                .andExpect(jsonPath("$.available", is(roomDto.isAvailable())))
                .andExpect(jsonPath("$.hotelDto", notNullValue()));
    }

    @Test
    void updateRoom() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));
        Room room = roomRepository.save(getRoom().setHotel(hotel));

        RoomDto roomDto = roomMapper.roomToRoomDto(room);
        roomDto
                .setId(room.getId())
                .setPrice(300)
                .setAvailable(false);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/rooms/" + room.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber()), Integer.class))
                .andExpect(jsonPath("$.price", is(roomDto.getPrice()), Integer.class))
                .andExpect(jsonPath("$.available", is(roomDto.isAvailable())))
                .andExpect(jsonPath("$.hotelDto", notNullValue()));
    }

    @Test
    void deleteRoom() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));
        Room room = roomRepository.save(getRoom().setHotel(hotel));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/rooms/" + room.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Room> empty = roomRepository.findById(room.getId());

        assertFalse(empty.isPresent());
    }

    private Room getRoom() {
        return Room.builder()
                .id(1L)
                .number(101)
                .price(250)
                .isAvailable(true)
                .hotel(getHotel())
                .build();
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