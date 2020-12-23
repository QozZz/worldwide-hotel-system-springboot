package com.qozz.worldwidehotelsystem.controller;

import com.qozz.worldwidehotelsystem.data.dto.AddressDto;
import com.qozz.worldwidehotelsystem.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> findAll() {
        return addressService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto findById(@PathVariable Long id) {
        return addressService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto createAddress(@RequestBody AddressDto addressDto) {
        return addressService.createAddress(addressDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AddressDto updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        return addressService.updateAddress(id, addressDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
