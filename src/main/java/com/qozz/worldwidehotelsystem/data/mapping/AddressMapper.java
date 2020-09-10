package com.qozz.worldwidehotelsystem.data.mapping;

import com.qozz.worldwidehotelsystem.data.dto.AddressDto;
import com.qozz.worldwidehotelsystem.data.entity.Address;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AddressMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "country", source = "country"),
            @Mapping(target = "city", source = "city"),
            @Mapping(target = "street", source = "street"),
            @Mapping(target = "number", source = "number"),

    })
    AddressDto addressToAddressDto(Address address);

    @InheritInverseConfiguration
    Address addressDtoToAddress(AddressDto addressDto);
}
