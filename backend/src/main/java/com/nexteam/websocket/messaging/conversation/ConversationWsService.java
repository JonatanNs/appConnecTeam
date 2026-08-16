package com.nexteam.websocket.messaging.conversation;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.Conversation.Conversation;
import com.nexteam.features.Messaging.Conversation.ConversationRepository;
import com.nexteam.features.Messaging.Message.Message;
import com.nexteam.features.Messaging.Message.MessageRepository;
import com.nexteam.features.Messaging.Message.dtos.MessageResponseDTO;
import com.nexteam.features.Messaging.Message.dtos.mapper.MessageMapper;
import com.nexteam.features.Messaging.Message.enums.MessageType;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Class 'ConversationWsService' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 22:48
 */
@Service
@RequiredArgsConstructor
public class ConversationWsService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Transactional
    public MessageResponseDTO createSystemMessage(UUID conversationId, String userEmail, String action) {
        Conversation conversation = conversationRepository.findByPublicId(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(user)
                .content(user.getFirstname() + " " + action)
                .type(MessageType.SYSTEM)
                .build();

        return messageMapper.messageToResponseDTO(messageRepository.save(message));
    }
}
