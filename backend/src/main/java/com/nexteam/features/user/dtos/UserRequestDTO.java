package com.nexteam.features.user.dtos;

import com.nexteam.features.Role.Role;
import com.nexteam.features.address.Address;
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
    private boolean active;
    private Set<Role> roles;
    private Address address;
}
