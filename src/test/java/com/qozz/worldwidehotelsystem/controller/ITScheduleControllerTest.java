package com.qozz.worldwidehotelsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozz.worldwidehotelsystem.data.dto.RentRoomDto;
import com.qozz.worldwidehotelsystem.data.entity.*;
import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import com.qozz.worldwidehotelsystem.data.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ITScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        scheduleRepository.deleteAll();
        roomRepository.deleteAll();
        hotelRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAll() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));
        Room room = roomRepository.save(getRoom().setHotel(hotel));
        User user = userRepository.save(getUser());

        Schedule schedule = getSchedule()
                .setUser(user)
                .setRoom(room);

        Schedule save = scheduleRepository.save(schedule);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(save.getId()), Long.class))
                .andExpect(jsonPath("$[0].roomDto.id", is(save.getRoom().getId()), Long.class))
                .andExpect(jsonPath("$[0].userEmail", is(save.getUser().getEmail())))
                .andExpect(jsonPath("$[0].rentStart", is("2020-09-18")))
                .andExpect(jsonPath("$[0].rentEnd", is("2020-09-25")));

    }

    @Test
    void createSchedule() throws Exception {
        Address address = addressRepository.save(getAddress());
        Hotel hotel = hotelRepository.save(getHotel().setAddress(address));
        Room room = roomRepository.save(getRoom().setHotel(hotel));
        User user = userRepository.save(getUser());

        RentRoomDto rentRoomDto = getRentRoomDto().setRoomId(room.getId());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                "test@test.com", null, null
                ));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentRoomDto)))
                .andExpect(status().isCreated());
    }

    private Schedule getSchedule() {
        return Schedule.builder()
                .id(1L)
                .user(getUser())
                .room(getRoom())
                .rentStart(LocalDate.of(2020, 9, 18))
                .rentEnd(LocalDate.of(2020, 9, 25))
                .build();
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .password(passwordEncoder.encode("pass1"))
                .roles(Collections.asSet(Role.USER))
                .build();

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

    private RentRoomDto getRentRoomDto() {
        return RentRoomDto.builder()
                .roomId(1L)
                .rentStart(LocalDate.of(2020, 9, 18))
                .rentEnd(LocalDate.of(2020, 9, 25))
                .build();
    }
}