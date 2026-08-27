package com.nexteam.features.Messaging.conversation.dtos;

import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import com.nexteam.features.Messaging.message.dtos.MessageResponseDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Classe 'ConvResponseDTO' en charge d'exposer les informations nécessaires au client sans
 * exposer directement l'entité 'Conversation'.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvResponseDTO {
    private UUID publicId;
    private Set<UserResponseDTO> users;
    private String name;
    private UserResponseDTO owner;
    private Instant createdAt;
    private List<MessageResponseDTO> messages;
}
