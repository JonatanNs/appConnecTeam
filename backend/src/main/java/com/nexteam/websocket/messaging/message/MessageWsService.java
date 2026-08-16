package com.nexteam.websocket.messaging.message;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.Conversation.Conversation;
import com.nexteam.features.Messaging.Conversation.ConversationRepository;
import com.nexteam.features.Messaging.Message.Message;
import com.nexteam.features.Messaging.Message.MessageRepository;
import com.nexteam.features.Messaging.Message.dtos.MessageRequestDTO;
import com.nexteam.features.Messaging.Message.dtos.MessageResponseDTO;
import com.nexteam.features.Messaging.Message.dtos.mapper.MessageMapper;
import com.nexteam.features.Messaging.Message.enums.MessageType;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Class 'MessageWsService' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 22:33
 */
@Service
@RequiredArgsConstructor
public class MessageWsService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public List<MessageResponseDTO> getMessages(UUID conversationId, Instant before, int limit) {
        conversationRepository.findByPublicId(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        Instant cursor = (before != null) ? before : Instant.now();
        Pageable pageable = PageRequest.of(0, limit);

        return messageRepository.findMessagesBefore(conversationId, cursor, pageable).stream()
                .map(messageMapper::messageToResponseDTO)
                .toList();
    }

    @Transactional
    public MessageResponseDTO sendMessage(UUID conversationId, MessageRequestDTO requestDTO, String senderEmail) {
        Conversation conversation = conversationRepository.findByPublicId(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(requestDTO.getContent())
                .type(MessageType.CHAT)
                .build();

        return messageMapper.messageToResponseDTO(messageRepository.save(message));
    }

}
