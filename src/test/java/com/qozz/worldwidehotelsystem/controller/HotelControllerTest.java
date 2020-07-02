package com.qozz.worldwidehotelsystem.controller;

import com.google.common.collect.ImmutableList;
import com.qozz.worldwidehotelsystem.data.dto.HotelInfoDto;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.service.HotelService;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class HotelControllerTest {

    private static final String HOTELS_ENDPOINT = "/hotels/";

    private static final int EXPECTED_ID = 1;

    private static final String HOTEL_NAME = "HotelOne";
    private static final int HOTEL_STARS = 5;
    private static final String HOTEL_COUNTRY = "CountryOne";
    private static final String HOTEL_CITY = "CityOne";
    private static final String HOTEL_STREET = "StreetOne";
    private static final String HOTEL_STREET_NUMBER = "1";
    private static final long HOTEL_ID = 1L;

    private String jsonHotel;
    private Hotel hotel;
    private HotelInfoDto hotelInfoDto;
    private List<HotelInfoDto> hotelsInfo;

    @InjectMocks
    private HotelController controller;

    @Mock
    private HotelService hotelService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws IOException {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        hotel = initHotel();
        hotelInfoDto = initHotelInfoDto();
        hotelsInfo = initHotelsInfo();
        jsonHotel = readJsonWithFile("json/HotelJSON.json");
    }

    @Test
    public void getHotelById() throws Exception {
        when(hotelService.getHotelById(HOTEL_ID)).thenReturn(hotelInfoDto);

        mockMvc.perform(get(HOTELS_ENDPOINT + HOTEL_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID)))
                .andExpect(jsonPath("$.name", is(HOTEL_NAME)))
                .andExpect(jsonPath("$.stars", is(HOTEL_STARS)))
                .andExpect(jsonPath("$.country", is(HOTEL_COUNTRY)))
                .andExpect(jsonPath("$.city", is(HOTEL_CITY)))
                .andExpect(jsonPath("$.street", is(HOTEL_STREET)))
                .andExpect(jsonPath("$.number", is(HOTEL_STREET_NUMBER)));

        verify(hotelService).getHotelById(HOTEL_ID);
    }

    @Test
    public void getAllHotelsInfo() throws Exception {
        when(hotelService.getHotelsInfo(anyString(), anyString())).thenReturn(hotelsInfo);

        mockMvc.perform(get(HOTELS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(EXPECTED_ID)))
                .andExpect(jsonPath("$[0].name", is(HOTEL_NAME)))
                .andExpect(jsonPath("$[0].stars", is(HOTEL_STARS)))
                .andExpect(jsonPath("$[0].country", is(HOTEL_COUNTRY)))
                .andExpect(jsonPath("$[0].city", is(HOTEL_CITY)))
                .andExpect(jsonPath("$[0].street", is(HOTEL_STREET)))
                .andExpect(jsonPath("$[0].number", is(HOTEL_STREET_NUMBER)));

        verify(hotelService).getHotelsInfo(anyString(), anyString());
    }

    @Test
    public void createHotel() throws Exception {
        when(hotelService.createHotel(hotel)).thenReturn(hotelInfoDto);

        mockMvc.perform(post(HOTELS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonHotel))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID)))
                .andExpect(jsonPath("$.name", is(HOTEL_NAME)))
                .andExpect(jsonPath("$.stars", is(HOTEL_STARS)))
                .andExpect(jsonPath("$.country", is(HOTEL_COUNTRY)))
                .andExpect(jsonPath("$.city", is(HOTEL_CITY)))
                .andExpect(jsonPath("$.street", is(HOTEL_STREET)))
                .andExpect(jsonPath("$.number", is(HOTEL_STREET_NUMBER)));

        verify(hotelService).createHotel(hotel);
    }

    @Test
    public void changeHotel() throws Exception {
        when(hotelService.changeHotel(hotel, HOTEL_ID)).thenReturn(hotelInfoDto);

        mockMvc.perform(put(HOTELS_ENDPOINT + HOTEL_ID).contentType(MediaType.APPLICATION_JSON).content(jsonHotel))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID)))
                .andExpect(jsonPath("$.name", is(HOTEL_NAME)))
                .andExpect(jsonPath("$.stars", is(HOTEL_STARS)))
                .andExpect(jsonPath("$.country", is(HOTEL_COUNTRY)))
                .andExpect(jsonPath("$.city", is(HOTEL_CITY)))
                .andExpect(jsonPath("$.street", is(HOTEL_STREET)))
                .andExpect(jsonPath("$.number", is(HOTEL_STREET_NUMBER)));

        verify(hotelService).changeHotel(hotel, HOTEL_ID);
    }

    @Test
    public void deleteHotel() throws Exception {
        doNothing().when(hotelService).deleteHotelById(HOTEL_ID);

        mockMvc.perform(delete(HOTELS_ENDPOINT + HOTEL_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(hotelService).deleteHotelById(HOTEL_ID);
    }

    private Hotel initHotel() {
        return new Hotel()
                .setId(HOTEL_ID)
                .setName(HOTEL_NAME)
                .setStars(HOTEL_STARS)
                .setCountry(HOTEL_COUNTRY)
                .setCity(HOTEL_CITY)
                .setStreet(HOTEL_STREET)
                .setNumber(HOTEL_STREET_NUMBER);
    }

    private HotelInfoDto initHotelInfoDto() {
        return new HotelInfoDto()
                .setId(HOTEL_ID)
                .setName(HOTEL_NAME)
                .setStars(HOTEL_STARS)
                .setCountry(HOTEL_COUNTRY)
                .setCity(HOTEL_CITY)
                .setStreet(HOTEL_STREET)
                .setNumber(HOTEL_STREET_NUMBER);
    }

    private List<HotelInfoDto> initHotelsInfo() {
        return ImmutableList.of(
                new HotelInfoDto()
                        .setId(HOTEL_ID)
                        .setName(HOTEL_NAME)
                        .setStars(HOTEL_STARS)
                        .setCountry(HOTEL_COUNTRY)
                        .setCity(HOTEL_CITY)
                        .setStreet(HOTEL_STREET)
                        .setNumber(HOTEL_STREET_NUMBER));
    }

    private String readJsonWithFile(String jsonFile) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFile);
        assert inputStream != null;
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }
}
