package com.nexteam.features.Users.Address.dtos;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private UUID publicId;
    private String street;
    private String city;
    private String country;
    private String zipcode;
    private Instant createdAt;
    private Instant updatedAt;
}

