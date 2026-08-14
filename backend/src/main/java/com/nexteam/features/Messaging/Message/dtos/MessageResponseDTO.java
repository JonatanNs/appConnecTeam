package com.nexteam.features.Messaging.Message.dtos;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    private UUID publicId;
    private List<String> content;
    private Instant updatedAt;
}
