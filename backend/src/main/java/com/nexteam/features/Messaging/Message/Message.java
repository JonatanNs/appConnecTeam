package com.nexteam.features.Messaging.Message;

import com.nexteam.common.AuditableEntity;
import com.nexteam.features.Messaging.Conversation.Conversation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

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

    @Lob
    @Column(nullable = false)
    @NotBlank
    private List<String> content;
}
