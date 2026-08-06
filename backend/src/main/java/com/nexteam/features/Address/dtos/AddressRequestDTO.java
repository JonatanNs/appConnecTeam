package com.nexteam.features.Address.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {
    private String street;
    private String city;
    private String state;
    private String zipCode;
}
