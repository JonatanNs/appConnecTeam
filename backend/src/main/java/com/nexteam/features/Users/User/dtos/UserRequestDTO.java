package com.nexteam.features.Users.User.dtos;

import com.nexteam.features.Users.Address.dtos.AddressRequestDTO;
import com.nexteam.features.Users.Role.dtos.RoleResponseDTO;
import lombok.*;

import java.util.Set;

/**
 * Classe 'UserRequestDTO' en charge de contrôler les données entrantes de l'API et d'éviter
 * d'exposer directement l'entité 'User' utilisée pour la persistance.
 *
 * @author jnsualu2026
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String firstname;
    private String lastname;
    private String email;
    private Set<RoleResponseDTO> roles;
    private AddressRequestDTO address;
}
