package com.qozz.worldwidehotelsystem.controller;

import com.google.common.collect.ImmutableList;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.service.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RoomControllerTest {

    private static final String ROOMS_ENDPOINT = "/hotels/1/rooms/";
    private static final String RENT_ROOM_ENDPOINT = ROOMS_ENDPOINT + "/rent/1";

    private static final Long ROOM_ID = 1L;
    private static final int ROOM_FLOOR = 1;
    private static final int ROOM_NUMBER = 101;
    private static final int EXPECTED_ROOM_ID = 1;

    private static final String HOTEL_NAME = "HotelOne";
    private static final int HOTEL_STARS = 5;
    private static final String HOTEL_COUNTRY = "CountryOne";
    private static final String HOTEL_CITY = "CityOne";
    private static final String HOTEL_STREET = "StreetOne";
    private static final String HOTEL_STREET_NUMBER = "1";
    private static final long HOTEL_ID = 1L;
    private static final int EXPECTED_HOTEL_ID = 1;

    private static final Long SCHEDULE_ID = 1L;
    private static final int EXPECTED_SCHEDULE_ID = 1;

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final int EXPECTED_USER_ID = 1;

    private User user;
    private Hotel hotel;
    private Room room;
    private List<Room> rooms;
    private LocalDate start;
    private LocalDate end;
    private Schedule schedule;

    @InjectMocks
    private RoomController controller;

    @Mock
    private RoomService roomService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = initUser();
        hotel = initHotel();
        room = initRoom();
        rooms = initRooms();
        start = LocalDate.of(2020, 1, 1);
        end = LocalDate.of(2020, 12, 12);
        schedule = initSchedule();
    }

    @Test
    public void getRoom() throws Exception {
        when(roomService.getRoomById(ROOM_ID)).thenReturn(room);

        mockMvc.perform(get(ROOMS_ENDPOINT + ROOM_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_ROOM_ID)))
                .andExpect(jsonPath("$.floor", is(ROOM_FLOOR)))
                .andExpect(jsonPath("$.number", is(ROOM_NUMBER)))
                .andExpect(jsonPath("$.hotel.id", is(EXPECTED_HOTEL_ID)))
                .andExpect(jsonPath("$.hotel.name", is(HOTEL_NAME)))
                .andExpect(jsonPath("$.hotel.stars", is(HOTEL_STARS)))
                .andExpect(jsonPath("$.hotel.country", is(HOTEL_COUNTRY)))
                .andExpect(jsonPath("$.hotel.city", is(HOTEL_CITY)))
                .andExpect(jsonPath("$.hotel.street", is(HOTEL_STREET)))
                .andExpect(jsonPath("$.hotel.number", is(HOTEL_STREET_NUMBER)));

        verify(roomService).getRoomById(ROOM_ID);
    }

    @Test
    public void getFreeHotelRooms() throws Exception {
        when(roomService.getFreeRoomsInHotel(HOTEL_ID, start, end)).thenReturn(rooms);

        mockMvc.perform(get(ROOMS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(EXPECTED_ROOM_ID)))
                .andExpect(jsonPath("$[0].floor", is(ROOM_FLOOR)))
                .andExpect(jsonPath("$[0].number", is(ROOM_NUMBER)))
                .andExpect(jsonPath("$[0].hotel.id", is(EXPECTED_HOTEL_ID)))
                .andExpect(jsonPath("$[0].hotel.name", is(HOTEL_NAME)))
                .andExpect(jsonPath("$[0].hotel.stars", is(HOTEL_STARS)))
                .andExpect(jsonPath("$[0].hotel.country", is(HOTEL_COUNTRY)))
                .andExpect(jsonPath("$[0].hotel.city", is(HOTEL_CITY)))
                .andExpect(jsonPath("$[0].hotel.street", is(HOTEL_STREET)))
                .andExpect(jsonPath("$[0].hotel.number", is(HOTEL_STREET_NUMBER)));

        verify(roomService).getFreeRoomsInHotel(HOTEL_ID, start, end);
    }

    @Test
    public void rentRoom() throws Exception {
        when(roomService.rentRoom(ROOM_ID, start, end, null)).thenReturn(schedule);

        mockMvc.perform(post(RENT_ROOM_ENDPOINT).contentType(APPLICATION_JSON)
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_SCHEDULE_ID)))
                .andExpect(jsonPath("$.registerStart[0]", is(start.getYear())))
                .andExpect(jsonPath("$.registerStart[1]", is(start.getMonthValue())))
                .andExpect(jsonPath("$.registerStart[2]", is(start.getDayOfMonth())))
                .andExpect(jsonPath("$.registerEnd[0]", is(end.getYear())))
                .andExpect(jsonPath("$.registerEnd[1]", is(end.getMonthValue())))
                .andExpect(jsonPath("$.registerEnd[2]", is(end.getDayOfMonth())))
                .andExpect(jsonPath("$.user.id", is(EXPECTED_USER_ID)))
                .andExpect(jsonPath("$.room.id", is(EXPECTED_ROOM_ID)));

        verify(roomService).rentRoom(ROOM_ID, start, end, null);
    }

    private User initUser() {
        return new User()
                .setId(USER_ID)
                .setUsername(USER_NAME)
                .setPassword(USER_PASSWORD);
    }

    private Room initRoom() {
        return new Room()
                .setId(ROOM_ID)
                .setFloor(ROOM_FLOOR)
                .setNumber(ROOM_NUMBER)
                .setHotel(hotel);
    }

    private List<Room> initRooms() {
        return ImmutableList.of(
                new Room()
                        .setId(ROOM_ID)
                        .setFloor(ROOM_FLOOR)
                        .setNumber(ROOM_NUMBER)
                        .setHotel(hotel));
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

    private Schedule initSchedule() {
        return new Schedule()
                .setId(SCHEDULE_ID)
                .setUser(user)
                .setRoom(room)
                .setRegisterStart(start)
                .setRegisterEnd(end);
    }
}
