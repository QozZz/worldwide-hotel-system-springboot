package com.qozz.worldwidehotelsystem.service;

import com.qozz.worldwidehotelsystem.data.dto.AddressDto;
import com.qozz.worldwidehotelsystem.data.entity.Address;
import com.qozz.worldwidehotelsystem.data.mapping.AddressMapper;
import com.qozz.worldwidehotelsystem.data.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
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
                .orElseThrow(() -> new RuntimeException("..."));
    }

    public AddressDto createAddress(AddressDto addressDto) {
        if (addressRepository.existsByCountryAndCityAndStreetAndNumber(
                addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), addressDto.getNumber())) {
            throw new RuntimeException("...");
        }

        Address address = addressMapper.addressDtoToAddress(addressDto);

        Address save = addressRepository.save(address);

        return addressMapper.addressToAddressDto(save);
    }

    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        if (addressRepository.existsByCountryAndCityAndStreetAndNumber(
                addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), addressDto.getNumber())) {
            throw new RuntimeException("...");
        }

        Address address = addressRepository.findById(id)
                .map(addressDb -> {
                    addressDb.setCountry(addressDto.getCountry());
                    addressDb.setCity(addressDto.getCity());
                    addressDb.setStreet(addressDto.getStreet());
                    addressDb.setNumber(addressDto.getNumber());
                    return addressDb;
                })
                .orElseThrow(() -> new RuntimeException("..."));

        Address save = addressRepository.save(address);

        return addressMapper.addressToAddressDto(save);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
