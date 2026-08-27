package com.nexteam.features.Messaging.message.dtos;

import com.nexteam.features.Messaging.message.enums.MessageType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Classe 'MessageResponseDTO' en charge d'exposer les informations nécessaires au client sans
 *  exposer directement l'entité 'Message'.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private UUID publicId;
    private String content;
    private UUID senderPublicId;
    private String senderName;
    private MessageType type;
    private Instant createdAt;
}