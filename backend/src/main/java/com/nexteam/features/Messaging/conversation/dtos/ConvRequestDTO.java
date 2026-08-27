package com.nexteam.features.Messaging.conversation.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

/**
 * Classe 'ConvRequestDTO' en charge de transmettre les informations nécessaires
 * à la création ou à la modification d'une conversation.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvRequestDTO {
    private Set<UUID> usersIds;
    @Size(max = 255, message = "Le nom ne peut pas dépasser {max} caractères.")
    private String name;
}
