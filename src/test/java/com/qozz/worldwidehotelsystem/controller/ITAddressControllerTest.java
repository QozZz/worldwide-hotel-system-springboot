package com.qozz.worldwidehotelsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozz.worldwidehotelsystem.data.dto.AddressDto;
import com.qozz.worldwidehotelsystem.data.entity.Address;
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
class ITAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @BeforeEach
    void setUp() {
        scheduleRepository.deleteAll();
        roomRepository.deleteAll();
        hotelRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findAll() throws Exception {
        Address save = addressRepository.save(getAddress());
        AddressDto addressDto = getAddressDto().setId(save.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(addressDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].country", is(addressDto.getCountry())))
                .andExpect(jsonPath("$[0].city", is(addressDto.getCity())))
                .andExpect(jsonPath("$[0].street", is(addressDto.getStreet())))
                .andExpect(jsonPath("$[0].number", is(addressDto.getNumber())));
    }

    @Test
    void findById() throws Exception {
        Address save = addressRepository.save(getAddress());
        AddressDto addressDto = getAddressDto().setId(save.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/addresses/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(addressDto.getId()), Long.class))
                .andExpect(jsonPath("$.country", is(addressDto.getCountry())))
                .andExpect(jsonPath("$.city", is(addressDto.getCity())))
                .andExpect(jsonPath("$.street", is(addressDto.getStreet())))
                .andExpect(jsonPath("$.number", is(addressDto.getNumber())));
    }

    @Test
    void createAddress() throws Exception {
        AddressDto addressDto = getAddressDto();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getAddressDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.country", is(addressDto.getCountry())))
                .andExpect(jsonPath("$.city", is(addressDto.getCity())))
                .andExpect(jsonPath("$.street", is(addressDto.getStreet())))
                .andExpect(jsonPath("$.number", is(addressDto.getNumber())));
    }

    @Test
    void updateAddress() throws Exception {
        Address save = addressRepository.save(getAddress());

        AddressDto addressDto = AddressDto.builder()
                .id(save.getId())
                .country("newCountry")
                .city("newCity")
                .street("newStreet")
                .number("777")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/addresses/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.country", is(addressDto.getCountry())))
                .andExpect(jsonPath("$.city", is(addressDto.getCity())))
                .andExpect(jsonPath("$.street", is(addressDto.getStreet())))
                .andExpect(jsonPath("$.number", is(addressDto.getNumber())));
    }

    @Test
    void deleteAddress() throws Exception {
        Address save = addressRepository.save(getAddress());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/addresses/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Address> empty = addressRepository.findById(save.getId());

        assertFalse(empty.isPresent());
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

    private AddressDto getAddressDto() {
        return AddressDto.builder()
                .id(1L)
                .country("Country X")
                .city("City X")
                .street("Street X")
                .number("1")
                .build();
    }
}