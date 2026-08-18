package com.nexteam.features.Users.User.dtos;

import com.nexteam.features.Users.Address.dtos.AddressResponseDTO;
import com.nexteam.features.Users.Role.dtos.RoleResponseDTO;
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
    private Set<RoleResponseDTO> roles;
    private AddressResponseDTO address;
    private Instant createdAt;
    private Instant updatedAt;

    public String getFullname() {
        return firstname + " " + lastname;
    }
}