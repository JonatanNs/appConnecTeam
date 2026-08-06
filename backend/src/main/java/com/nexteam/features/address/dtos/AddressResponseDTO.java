package com.nexteam.features.address.dtos;


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
    private String state;
    private String zipCode;
    private Instant createdAt;
    private Instant updatedAt;
}

