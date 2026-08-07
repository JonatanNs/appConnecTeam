package com.nexteam.features.Address.dtos;


import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private String publicId;
    private String street;
    private String city;
    private String country;
    private String zipcode;
    private Instant createdAt;
    private Instant updatedAt;
}

