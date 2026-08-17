package com.nexteam.features.notification;

import com.nexteam.common.AuditableEntity;
import com.nexteam.features.Conversation.Conversation;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.notification.enums.NotificationType;
import com.nexteam.websocket.messaging.message.Message;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Class 'Notification' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 21:56
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
public class Notification extends AuditableEntity {
    @Column(nullable = false)
    private String content;
    private Boolean read;
    private Instant readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    @ToString.Exclude
    private User recipient;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    @ToString.Exclude
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    @ToString.Exclude
    private Message message;
}
