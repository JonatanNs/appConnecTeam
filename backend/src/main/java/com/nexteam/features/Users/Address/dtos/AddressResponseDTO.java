package com.nexteam.features.Users.Address.dtos;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Classe 'AddressResponseDTO' en charge d'exposer les informations nécessaires au client sans
 *  exposer directement l'entité 'Adresse'.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
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

