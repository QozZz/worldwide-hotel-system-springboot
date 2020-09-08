//package com.qozz.worldwidehotelsystem.service;
//
//import com.google.common.collect.ImmutableList;
//import com.qozz.worldwidehotelsystem.data.dto.HotelInfoDto;
//import com.qozz.worldwidehotelsystem.data.entity.Hotel;
//import com.qozz.worldwidehotelsystem.data.mapping.HotelMapper;
//import com.qozz.worldwidehotelsystem.data.repository.HotelRepository;
//import com.qozz.worldwidehotelsystem.exception.HotelDoesNotExistException;
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
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static com.qozz.worldwidehotelsystem.exception.ExceptionMessages.HOTEL_DOES_NOT_EXIST;
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class HotelServiceTest {
//
//    private Hotel hotel;
//    private List<Hotel> hotelList;
//    private List<HotelInfoDto> hotelInfoDtoList;
//
//    @Rule
//    public ExpectedException expectedEx = ExpectedException.none();
//
//    @InjectMocks
//    private HotelService hotelService;
//
//    @Mock
//    private HotelRepository hotelRepository;
//
//    @Spy
//    private HotelMapper hotelMapper = Mappers.getMapper(HotelMapper.class);
//
//    @Before
//    public void setUp() {
//        hotel = initHotel();
//        hotelList = initHotelList();
//        hotelInfoDtoList = initHotelInfoDtoList();
//    }
//
//    @Test
//    public void getHotelByIdWhenHotelExists() {
//        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
//
//        hotelService.getHotelById(1L);
//
//        verify(hotelRepository).findById(1L);
//    }
//
//    @Test
//    public void getHotelByIdWhenHotelDoesNotExist() {
//        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
//
//        expectedEx.expect(HotelDoesNotExistException.class);
//        expectedEx.expectMessage(HOTEL_DOES_NOT_EXIST);
//
//        hotelService.getHotelById(1L);
//    }
//
//    @Test
//    public void getHotelsInfoWhenHotelsExist() {
//        when(hotelRepository.findAllHotelsByCountryAndCity(anyString(), anyString()))
//                .thenReturn(hotelList);
//
//        List<HotelInfoDto> hotelInfoDtoList = hotelService.getHotelsInfo(anyString(), anyString());
//
//        verify(hotelRepository).findAllHotelsByCountryAndCity(anyString(), anyString());
//
//        assertFalse(hotelInfoDtoList.isEmpty());
//    }
//
//    @Test
//    public void getHotelsInfoWhenHotelsDoesNotExist() {
//        List<Hotel> emptyHotelList = Collections.emptyList();
//        when(hotelRepository.findAllHotelsByCountryAndCity(anyString(), anyString()))
//                .thenReturn(emptyHotelList);
//
//        List<HotelInfoDto> hotelInfoDtoList = hotelService.getHotelsInfo(anyString(), anyString());
//
//        verify(hotelRepository).findAllHotelsByCountryAndCity(anyString(), anyString());
//
//        assertTrue(hotelInfoDtoList.isEmpty());
//    }
//
//    @Test
//    public void createHotel() {
//        when(hotelRepository.saveAndFlush(any(Hotel.class))).thenReturn(hotel);
//
//        HotelInfoDto savedHotel = hotelService.createHotel(new Hotel());
//
//        verify(hotelRepository).saveAndFlush(any(Hotel.class));
//
//        assertNotNull(savedHotel);
//    }
//
//    @Test
//    public void changeHotelWhenHotelExists() {
//        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
//        when(hotelRepository.saveAndFlush(any(Hotel.class))).thenReturn(hotel);
//
//        HotelInfoDto changedHotel = hotelService.changeHotel(new Hotel(), 1L);
//
//        verify(hotelRepository).findById(1L);
//        verify(hotelRepository).saveAndFlush(any(Hotel.class));
//
//        assertNotNull(changedHotel);
//    }
//
//    @Test
//    public void changeHotelWhenHotelDoesNotExist() {
//        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
//
//        expectedEx.expect(HotelDoesNotExistException.class);
//        expectedEx.expectMessage(HOTEL_DOES_NOT_EXIST);
//
//        hotelService.changeHotel(new Hotel(), 1L);
//
//        verify(hotelRepository).saveAndFlush(any(Hotel.class));
//    }
//
//    @Test
//    public void deleteHotelWhenHotelExists() {
//        doNothing().when(hotelRepository).deleteHotelById(1L);
//        hotelService.deleteHotelById(1L);
//
//        verify(hotelRepository).deleteHotelById(1L);
//        verifyNoMoreInteractions(hotelRepository);
//    }
//
//    @Test
//    public void deleteHotelWhenHotelDoesNotExist() {
//        doNothing().when(hotelRepository).deleteHotelById(1L);
//
//        hotelService.deleteHotelById(1L);
//
//        verify(hotelRepository).deleteHotelById(1L);
//        verifyNoMoreInteractions(hotelRepository);
//    }
//
//    private List<Hotel> initHotelList() {
//        return ImmutableList.of(
//                new Hotel()
//                        .setId(1L)
//                        .setName("HotelOne")
//                        .setStars(5)
//                        .setCountry("CountryTwo")
//                        .setCity("CityTwo")
//                        .setStreet("StreetTwo")
//                        .setNumber("1"),
//                new Hotel()
//                        .setId(1L)
//                        .setName("HotelTwo")
//                        .setStars(5)
//                        .setCountry("CountryTwo")
//                        .setCity("CityTwo")
//                        .setStreet("StreetTwo")
//                        .setNumber("2"));
//    }
//
//    private List<HotelInfoDto> initHotelInfoDtoList() {
//        return ImmutableList.of(
//                new HotelInfoDto()
//                        .setId(1L)
//                        .setName("HotelOne")
//                        .setStars(5)
//                        .setCountry("CountryTwo")
//                        .setCity("CityTwo")
//                        .setStreet("StreetTwo")
//                        .setNumber("1"),
//                new HotelInfoDto()
//                        .setId(1L)
//                        .setName("HotelTwo")
//                        .setStars(5)
//                        .setCountry("CountryTwo")
//                        .setCity("CityTwo")
//                        .setStreet("StreetTwo")
//                        .setNumber("2"));
//    }
//
//    private Hotel initHotel() {
//        return new Hotel()
//                .setId(1L)
//                .setName("HotelOne")
//                .setStars(5)
//                .setCountry("CountryTwo")
//                .setCity("CityTwo")
//                .setStreet("StreetTwo")
//                .setNumber("1");
//    }
//}
