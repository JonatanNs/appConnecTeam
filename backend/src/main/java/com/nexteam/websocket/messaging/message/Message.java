package com.nexteam.websocket.messaging.message;

import com.nexteam.common.AuditableEntity;
import com.nexteam.features.Conversation.Conversation;
import com.nexteam.features.Users.User.User;
import com.nexteam.websocket.messaging.message.enums.MessageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
public class Message extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

}
