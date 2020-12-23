package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.AddressDto;
import com.qozz.worldwidehotelsystem.data.entity.Address;
import com.qozz.worldwidehotelsystem.data.mapping.AddressMapper;
import com.qozz.worldwidehotelsystem.data.repository.AddressRepository;
import com.qozz.worldwidehotelsystem.exception.EntityAlreadyExistsException;
import com.qozz.worldwidehotelsystem.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::addressToAddressDto)
                .collect(Collectors.toList());
    }

    public AddressDto findById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::addressToAddressDto)
                .orElseThrow(() -> new EntityNotFoundException("Address with Id [" + id + "] doesn't exist"));
    }

    public AddressDto createAddress(AddressDto addressDto) {
        if (addressRepository.existsByCountryAndCityAndStreetAndNumber(
                addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), addressDto.getNumber())) {
            throw new EntityAlreadyExistsException("Address with: " +
                    "Country [" + addressDto.getCountry() + "] " +
                    "City [" + addressDto.getCity() + "] " +
                    "Street [" + addressDto.getStreet() + "] " +
                    "Number [" + addressDto.getNumber() + "] already exists");
        }

        Address address = addressMapper.addressDtoToAddress(addressDto);

        Address save = addressRepository.save(address);

        return addressMapper.addressToAddressDto(save);
    }

    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        if (addressRepository.existsByCountryAndCityAndStreetAndNumber(
                addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), addressDto.getNumber())) {
            throw new EntityAlreadyExistsException("Address with: " +
                    "Country [" + addressDto.getCountry() + "] " +
                    "City [" + addressDto.getCity() + "] " +
                    "Street [" + addressDto.getStreet() + "] " +
                    "Number [" + addressDto.getNumber() + "] already exists");
        }

        Address address = addressRepository.findById(id)
                .map(addressDb -> {
                    addressDb.setCountry(addressDto.getCountry());
                    addressDb.setCity(addressDto.getCity());
                    addressDb.setStreet(addressDto.getStreet());
                    addressDb.setNumber(addressDto.getNumber());
                    return addressDb;
                })
                .orElseThrow(() -> new EntityNotFoundException("Address with Id [" + id + "] doesn't exist"));

        Address save = addressRepository.save(address);

        return addressMapper.addressToAddressDto(save);
    }

    public void deleteAddress(Long id) {
        try {
            addressRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Address with Id [" + id + "] doesn't exist");
        }
    }
}
