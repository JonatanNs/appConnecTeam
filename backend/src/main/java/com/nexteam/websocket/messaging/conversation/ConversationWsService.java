package com.nexteam.websocket.messaging.conversation;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Conversation.Conversation;
import com.nexteam.features.Conversation.ConversationRepository;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import com.nexteam.websocket.messaging.message.Message;
import com.nexteam.websocket.messaging.message.MessageRepository;
import com.nexteam.websocket.messaging.message.dtos.MessageResponseDTO;
import com.nexteam.websocket.messaging.message.dtos.mapper.MessageMapper;
import com.nexteam.websocket.messaging.message.enums.MessageType;
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
