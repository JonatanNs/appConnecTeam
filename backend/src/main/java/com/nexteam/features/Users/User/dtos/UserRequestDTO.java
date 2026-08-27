package com.nexteam.features.Users.User.dtos;

import com.nexteam.features.Users.Address.dtos.AddressRequestDTO;
import com.nexteam.features.Users.Role.dtos.RoleResponseDTO;
import lombok.*;

import java.util.Set;

/**
 * Classe 'UserRequestDTO' en charge de transmettre les informations nécessaires
 * à la création ou à la modification d'un rôle.
 *
 * @author jnsualu2026
 * @since 2026-08-27
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
    private String password;
    private Set<RoleResponseDTO> roles;
    private AddressRequestDTO address;
}
