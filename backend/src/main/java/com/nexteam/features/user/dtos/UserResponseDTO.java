package com.nexteam.features.user.dtos;

import com.nexteam.features.Role.Role;
import com.nexteam.features.address.Address;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Classe 'UserResponseDTO' en charge d'exposer les informations nécessaires au client sans
 * exposer directement l'entité 'User' persistée en base de données.
 *
 * @author jnsualu2026
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID publicId;
    private String firstname;
    private String lastname;
    private String email;
    private boolean active;
    private Set<Role> roles;
    private Address address;
    private Instant createdAt;
    private Instant updatedAt;
}