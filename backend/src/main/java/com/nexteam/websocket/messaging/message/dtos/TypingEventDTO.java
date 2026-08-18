package com.nexteam.websocket.messaging.message.dtos;

import lombok.*;

import java.util.UUID;

/**
 * Class 'TypingEventDTO' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 16:16
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypingEventDTO {
    private UUID conversationId;
    private UUID userId;
    private String userName;
    private boolean isTyping;
}
