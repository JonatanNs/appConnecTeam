package com.nexteam.features.User.dtos;

import com.nexteam.features.Address.Address;
import com.nexteam.features.Role.Role;
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
    private Set<Role> roles;
    private Address address;
}
