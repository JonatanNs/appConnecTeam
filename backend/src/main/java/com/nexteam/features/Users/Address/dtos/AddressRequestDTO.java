package com.nexteam.features.Users.Address.dtos;

import lombok.*;

/**
 * Classe 'AddressRequestDTO' en charge de transmettre les informations nécessaires
 * à la création ou à la modification d'une adresse.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {
    private String street;
    private String city;
    private String country;
    private String zipcode;
}
