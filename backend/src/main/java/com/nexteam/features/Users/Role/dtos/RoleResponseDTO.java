package com.nexteam.features.Users.Role.dtos;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Classe 'RoleResponseDTO' en charge d'exposer les informations nécessaires au client sans
 *  exposer directement l'entité 'Rôle'.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private UUID publicId;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
