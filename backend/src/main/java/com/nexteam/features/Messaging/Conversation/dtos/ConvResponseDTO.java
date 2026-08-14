package com.nexteam.features.Messaging.Conversation.dtos;

import com.nexteam.features.Messaging.Message.dtos.MessageResponseDTO;
import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvResponseDTO {
    private UUID publicId;
    private Set<UserResponseDTO> users;
    private String name;
    private Instant createdAt;
    private List<MessageResponseDTO> messages;
}
