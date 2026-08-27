package com.nexteam.features.Messaging.message.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Classe 'MessageRequestDTO' en charge de transmettre les informations nécessaires
 * à la création ou à la modification d'un message.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    @NotBlank(message = "Le contenu du message ne peut pas être vide.")
    private String content;
    private Instant createdAt;
    private UUID sender;
}
