package com.nexteam.features.Messaging.Message;

import com.nexteam.features.Messaging.Conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Conversation, UUID> {
}
