package com.qozz.worldwidehotelsystem.data.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Builder
public class AddressDto {

    private long id;
    private String country;
    private String city;
    private String street;
    private String number;

}
