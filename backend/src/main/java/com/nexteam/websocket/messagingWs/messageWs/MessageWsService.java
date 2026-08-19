package com.nexteam.websocket.messagingWs.messageWs;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.conversation.Conversation;
import com.nexteam.features.Messaging.conversation.ConversationRepository;
import com.nexteam.features.Messaging.conversation.ConversationService;
import com.nexteam.features.Messaging.message.Message;
import com.nexteam.features.Messaging.message.MessageRepository;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import com.nexteam.features.Messaging.notification.NotificationService;
import com.nexteam.websocket.messagingWs.conversationWs.ConversationPresenceService;
import com.nexteam.features.Messaging.message.dtos.MessageRequestDTO;
import com.nexteam.features.Messaging.message.dtos.MessageResponseDTO;
import com.nexteam.features.Messaging.message.dtos.mapper.MessageMapper;
import com.nexteam.features.Messaging.message.enums.MessageType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Class 'MessageService' en charge de
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
    private final ConversationPresenceService presenceService;
    private final NotificationService notificationService;
    private final ConversationService conversationService;

    @Transactional
    public MessageResponseDTO sendMessage(UUID conversationId, MessageRequestDTO requestDTO, String senderEmail) {
        Conversation conversation = conversationRepository.findByPublicId(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        boolean isParticipant = conversation.getUsers().stream()
                .anyMatch(u -> u.getPublicId().equals(sender.getPublicId()));
        if (!isParticipant) {
            throw new AccessDeniedException("Vous n'êtes pas participant de cette conversation.");
        }

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(requestDTO.getContent())
                .type(MessageType.CHAT)
                .build();

        Message saved = messageRepository.save(message);

        // Notifier les participants absents de la conversation à l'instant T
        conversation.getUsers().stream()
                .filter(participant -> !participant.getPublicId().equals(sender.getPublicId()))
                .filter(participant -> !presenceService.isOpen(participant.getEmail(), conversationId.toString()))
                .forEach(participant ->
                        notificationService.notifyNewMessage(
                                participant.getPublicId(),
                                conversationId,
                                saved.getPublicId(),
                                sender.getFirstname() + " " + sender.getLastname()));

        return messageMapper.messageToResponseDTO(saved);
    }
}
