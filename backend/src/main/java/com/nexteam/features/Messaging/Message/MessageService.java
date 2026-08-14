package com.nexteam.features.Messaging.Message;

import com.nexteam.features.Messaging.Conversation.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ConversationRepository conversationRepository;


}
