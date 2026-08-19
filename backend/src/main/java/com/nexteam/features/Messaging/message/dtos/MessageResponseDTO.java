package com.nexteam.features.Messaging.message.dtos;

import com.nexteam.features.Messaging.message.enums.MessageType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

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