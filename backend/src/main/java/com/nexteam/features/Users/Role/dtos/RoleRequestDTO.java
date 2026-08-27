package com.nexteam.features.Users.Role.dtos;

import lombok.*;

/**
 * Classe 'RoleRequestDTO' en charge de transmettre les informations nécessaires
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
public class RoleRequestDTO {
    private String name;
}
