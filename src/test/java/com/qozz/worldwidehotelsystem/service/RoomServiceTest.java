package com.qozz.worldwidehotelsystem.service;

import com.google.common.collect.ImmutableList;
import com.qozz.worldwidehotelsystem.data.entity.Hotel;
import com.qozz.worldwidehotelsystem.data.entity.Room;
import com.qozz.worldwidehotelsystem.data.entity.Schedule;
import com.qozz.worldwidehotelsystem.data.entity.User;
import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
import com.qozz.worldwidehotelsystem.exception.RoomAlreadyRentedException;
import com.qozz.worldwidehotelsystem.exception.RoomDoesNotExistException;
import com.qozz.worldwidehotelsystem.exception.UserDoesNotExistException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {

    private final static String ROOM_DOES_NOT_EXIST = "Room does not exist!";
    public final static String USER_DOES_NOT_EXIST = "User does not exist!";
    public static final String ROOM_IS_ALREADY_RENTED = "Room is already rented!";
    private List<Room> freeRooms;
    private Room room;
    private LocalDate start;
    private LocalDate end;
    private User user;
    private Schedule schedule;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        freeRooms = initFreeRoomsList();
        room = initRoom();
        start = LocalDate.of(2020, 1, 1);
        end = LocalDate.of(2020, 2, 1);
        user = initUser();
        schedule = initSchedule();
    }

    @Test
    public void getRoomByIdWhenRoomExists() {
        when(roomRepository.findRoomById(1L)).thenReturn(Optional.of(room));

        roomService.getRoomById(1L);

        verify(roomRepository).findRoomById(1L);
    }

    @Test
    public void getRoomByIdWhenRoomDoesNotExist() {
        when(roomRepository.findRoomById(1L)).thenReturn(Optional.empty());

        expectedEx.expect(RoomDoesNotExistException.class);
        expectedEx.expectMessage(ROOM_DOES_NOT_EXIST);

        roomService.getRoomById(1L);

        verify(roomRepository).findRoomById(1L);
    }

    @Test
    public void getFreeRoomsInHotelWhenFreeRoomsExist() {
        when(roomRepository.findAllFreeByHotelId(1L, start, end))
                .thenReturn(freeRooms);

        roomService.getFreeRoomsInHotel(1L, start, end);

        verify(roomRepository).findAllFreeByHotelId(1L, start, end);
    }

    @Test
    public void getFreeRoomsInHotelWhenFreeRoomsDoNotExist() {
        when(roomRepository.findAllFreeByHotelId(1L, start, end))
                .thenReturn(Collections.emptyList());

        roomService.getFreeRoomsInHotel(1L, start, end);

        verify(roomRepository).findAllFreeByHotelId(1L, start, end);
    }

    @Test
    public void rentRoom() {
        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(0);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(roomRepository.findRoomById(1L)).thenReturn(Optional.of(room));
        when(scheduleRepository.saveAndFlush(any(Schedule.class))).thenReturn(schedule);

        Schedule savedSchedule = roomService.rentRoom(1L, start, end, "username");

        verify(roomRepository).findNumberOfRentedRooms(1L, start, end);
        verify(userRepository).findByUsername(anyString());
        verify(roomRepository).findRoomById(1L);
        verify(scheduleRepository).saveAndFlush(any(Schedule.class));

        assertNotNull(savedSchedule);
    }

    @Test
    public void rentRoomWhenRoomRented() {
        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(1);

        expectedEx.expect(RoomAlreadyRentedException.class);
        expectedEx.expectMessage(ROOM_IS_ALREADY_RENTED);

        roomService.rentRoom(1L, start, end, "username");
    }

    @Test
    public void rentRoomWhenUserDoesNotExist() {
        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(0);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        expectedEx.expect(UserDoesNotExistException.class);
        expectedEx.expectMessage(USER_DOES_NOT_EXIST);

        roomService.rentRoom(1L, start, end, "username");
    }

    @Test
    public void rentRoomWhenRoomDoesNotExist() {
        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(0);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(roomRepository.findRoomById(1L)).thenReturn(Optional.empty());

        expectedEx.expect(RoomDoesNotExistException.class);
        expectedEx.expectMessage(ROOM_DOES_NOT_EXIST);

        roomService.rentRoom(1L, start, end, "username");
    }

    private Room initRoom() {
        return new Room()
                .setId(1L)
                .setFloor(1)
                .setNumber(101)
                .setHotel(
                        new Hotel()
                                .setId(1L)
                                .setName("hotelOne")
                                .setStars(5)
                                .setCountry("Country")
                                .setCity("CityOne")
                                .setStreet("StreetOne")
                                .setNumber("11"));
    }

    private List<Room> initFreeRoomsList() {
        Hotel hotel = new Hotel()
                .setId(1L)
                .setName("hotelOne")
                .setStars(5)
                .setCountry("Country")
                .setCity("CityOne")
                .setStreet("StreetOne")
                .setNumber("11");

        return ImmutableList.of(
                new Room()
                        .setId(1L)
                        .setFloor(1)
                        .setNumber(101)
                        .setHotel(hotel),
                new Room()
                        .setId(2L)
                        .setFloor(2)
                        .setNumber(201)
                        .setHotel(hotel));
    }

    private User initUser() {
        return new User()
                .setId(1L)
                .setUsername("username");
    }

    private Schedule initSchedule() {
        return new Schedule()
                .setUser(user)
                .setRoom(room)
                .setRegisterStart(start)
                .setRegisterEnd(end);
    }
}
