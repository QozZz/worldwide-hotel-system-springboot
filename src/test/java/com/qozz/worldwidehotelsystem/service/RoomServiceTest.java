//package com.qozz.worldwidehotelsystem.service;
//
//import com.google.common.collect.ImmutableList;
//import com.qozz.worldwidehotelsystem.data.dto.RentRoomDto;
//import com.qozz.worldwidehotelsystem.data.dto.RoomInfoDto;
//import com.qozz.worldwidehotelsystem.data.entity.Hotel;
//import com.qozz.worldwidehotelsystem.data.entity.Room;
//import com.qozz.worldwidehotelsystem.data.entity.Schedule;
//import com.qozz.worldwidehotelsystem.data.entity.User;
//import com.qozz.worldwidehotelsystem.data.mapping.RoomMapper;
//import com.qozz.worldwidehotelsystem.data.repository.RoomRepository;
//import com.qozz.worldwidehotelsystem.data.repository.ScheduleRepository;
//import com.qozz.worldwidehotelsystem.data.repository.UserRepository;
//import com.qozz.worldwidehotelsystem.exception.RoomAlreadyRentedException;
//import com.qozz.worldwidehotelsystem.exception.RoomDoesNotExistException;
//import com.qozz.worldwidehotelsystem.exception.UserDoesNotExistException;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mapstruct.factory.Mappers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDate;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static com.qozz.worldwidehotelsystem.exception.ExceptionMessages.*;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class RoomServiceTest {
//
//    private static final long HOTEL_ID = 1L;
//    private static final String HOTEL_NAME = "hotelOne";
//    private static final int HOTEL_STARS = 5;
//    private static final String HOTEL_COUNTRY = "Country";
//    private static final String HOTEL_CITY = "CityOne";
//    private static final String HOTEL_STREET = "StreetOne";
//    private static final String HOTEL_STREET_NUMBER = "11";
//
//    private static final long ROOM_ID = 1L;
//    private static final int ROOM_FLOOR = 1;
//    private static final int ROOM_NUMBER = 101;
//    private static final int ROOM_PRICE = 100;
//
//    private static final long USER_ID = 1L;
//    private static final String USER_NAME = "username";
//
//    private Hotel hotel;
//    private List<Room> freeRooms;
//    private List<RoomInfoDto> freeRoomInfoDtoList;
//    private Room room;
//    private LocalDate start;
//    private LocalDate end;
//    private User user;
//    private Schedule schedule;
//    private RentRoomDto rentRoomDto;
//
//    @Rule
//    public ExpectedException expectedEx = ExpectedException.none();
//
//    @InjectMocks
//    private RoomService roomService;
//
//    @Mock
//    private RoomRepository roomRepository;
//
//    @Mock
//    private ScheduleRepository scheduleRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Spy
//    private RoomMapper roomMapper = Mappers.getMapper(RoomMapper.class);
//
//    @Before
//    public void setUp() {
//        hotel = initHotel();
//        freeRooms = initFreeRoomsList();
//        freeRoomInfoDtoList = initFreeRoomInfoDtoList();
//        room = initRoom();
//        start = LocalDate.of(2020, 1, 1);
//        end = LocalDate.of(2020, 2, 1);
//        user = initUser();
//        schedule = initSchedule();
//        rentRoomDto = initRentRoomDto();
//    }
//
//    @Test
//    public void getRoomByIdWhenRoomExists() {
//        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
//
//        roomService.getRoomById(1L);
//
//        verify(roomRepository).findById(1L);
//    }
//
//    @Test
//    public void getRoomByIdWhenRoomDoesNotExist() {
//        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
//
//        expectedEx.expect(RoomDoesNotExistException.class);
//        expectedEx.expectMessage(ROOM_DOES_NOT_EXIST);
//
//        roomService.getRoomById(1L);
//
//        verify(roomRepository).findById(1L);
//    }
//
//    @Test
//    public void getFreeRoomsInHotelWhenFreeRoomsExist() {
//        when(roomRepository.findAllFreeByHotelId(1L, start, end))
//                .thenReturn(freeRooms);
//
//        roomService.getFreeRoomsInHotel(1L, start, end);
//
//        verify(roomRepository).findAllFreeByHotelId(1L, start, end);
//    }
//
//    @Test
//    public void getFreeRoomsInHotelWhenFreeRoomsDoNotExist() {
//        when(roomRepository.findAllFreeByHotelId(1L, start, end))
//                .thenReturn(Collections.emptyList());
//
//        roomService.getFreeRoomsInHotel(1L, start, end);
//
//        verify(roomRepository).findAllFreeByHotelId(1L, start, end);
//    }
//
//    @Test
//    public void rentRoom() {
//        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(0);
//        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
//        when(scheduleRepository.saveAndFlush(any(Schedule.class))).thenReturn(schedule);
//
//        Schedule savedSchedule = roomService.rentRoom(rentRoomDto, "username");
//
//        verify(roomRepository).findNumberOfRentedRooms(1L, start, end);
//        verify(userRepository).findUserByUsername(anyString());
//        verify(roomRepository).findById(1L);
//        verify(scheduleRepository).saveAndFlush(any(Schedule.class));
//
//        assertNotNull(savedSchedule);
//    }
//
//    @Test
//    public void rentRoomWhenRoomRented() {
//        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(1);
//
//        expectedEx.expect(RoomAlreadyRentedException.class);
//        expectedEx.expectMessage(ROOM_IS_ALREADY_RENTED);
//
//        roomService.rentRoom(rentRoomDto, "username");
//    }
//
//    @Test
//    public void rentRoomWhenUserDoesNotExist() {
//        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(0);
//        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
//
//        expectedEx.expect(UserDoesNotExistException.class);
//        expectedEx.expectMessage(USER_DOES_NOT_EXIST);
//
//        roomService.rentRoom(rentRoomDto, "username");
//    }
//
//    @Test
//    public void rentRoomWhenRoomDoesNotExist() {
//        when(roomRepository.findNumberOfRentedRooms(1L, start, end)).thenReturn(0);
//        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
//        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
//
//        expectedEx.expect(RoomDoesNotExistException.class);
//        expectedEx.expectMessage(ROOM_DOES_NOT_EXIST);
//
//        roomService.rentRoom(rentRoomDto, "username");
//    }
//
//    private Hotel initHotel() {
//        return new Hotel()
//                .setId(HOTEL_ID)
//                .setName(HOTEL_NAME)
//                .setStars(HOTEL_STARS)
//                .setCountry(HOTEL_COUNTRY)
//                .setCity(HOTEL_CITY)
//                .setStreet(HOTEL_STREET)
//                .setNumber(HOTEL_STREET_NUMBER);
//    }
//
//    private Room initRoom() {
//        return new Room()
//                .setId(ROOM_ID)
//                .setFloor(ROOM_FLOOR)
//                .setNumber(ROOM_NUMBER)
//                .setHotel(hotel);
//    }
//
//    private List<Room> initFreeRoomsList() {
//        return ImmutableList.of(
//                new Room()
//                        .setId(ROOM_ID)
//                        .setFloor(ROOM_FLOOR)
//                        .setNumber(ROOM_NUMBER)
//                        .setHotel(hotel));
//    }
//
//    private List<RoomInfoDto> initFreeRoomInfoDtoList() {
//        return ImmutableList.of(
//                new RoomInfoDto()
//                        .setHotelName(HOTEL_NAME)
//                        .setFloor(ROOM_FLOOR)
//                        .setNumber(ROOM_NUMBER)
//                        .setPrice(ROOM_PRICE)
//                        .setCountry(HOTEL_COUNTRY)
//                        .setCity(HOTEL_CITY)
//                        .setStreet(HOTEL_STREET)
//                        .setStreetNumber(HOTEL_STREET_NUMBER));
//    }
//
//    private User initUser() {
//        return new User()
//                .setId(USER_ID)
//                .setUsername(USER_NAME);
//    }
//
//    private Schedule initSchedule() {
//        return new Schedule()
//                .setUser(user)
//                .setRoom(room)
//                .setRentStart(start)
//                .setRentEnd(end);
//    }
//
//    private RentRoomDto initRentRoomDto() {
//        return new RentRoomDto()
//                .setId(ROOM_ID)
//                .setRentStart(start)
//                .setRentEnd(end);
//    }
//}
