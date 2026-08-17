package com.nexteam.features.notification.dtos;

import com.nexteam.features.notification.enums.NotificationType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private UUID publicId;
    private String content;
    private Boolean read;
    private Instant readAt;
    private NotificationType type;
    private UUID conversationPublicId;
    private UUID messagePublicId;
    private Instant createdAt;
}