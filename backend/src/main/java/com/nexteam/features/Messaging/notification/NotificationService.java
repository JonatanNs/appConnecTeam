package com.nexteam.features.Messaging.notification;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.conversation.Conversation;
import com.nexteam.features.Messaging.conversation.ConversationRepository;
import com.nexteam.features.Messaging.conversation.dtos.mapper.ConvMapper;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import com.nexteam.features.Messaging.notification.dtos.NotificationResponseDTO;
import com.nexteam.features.Messaging.notification.dtos.mapper.NotificationMapper;
import com.nexteam.features.Messaging.notification.enums.NotificationType;
import com.nexteam.features.Messaging.message.Message;
import com.nexteam.features.Messaging.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Class 'NotificationService' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 22:17
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final NotificationMapper notificationMapper;
    private final ConvMapper convMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void notifyNewMessage(UUID recipientPublicId, UUID conversationPublicId, UUID messagePublicId, String senderName) {
        User recipient = userRepository.findByPublicId(recipientPublicId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));
        Conversation conversation = conversationRepository.findByPublicId(conversationPublicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));
        Message message = messageRepository.findByPublicId(messagePublicId)
                .orElseThrow(() -> new NotFoundException("Message non trouvé."));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .conversation(conversation)
                .message(message)
                .type(NotificationType.NEW_MESSAGE)
                .content(senderName + " vous a envoyé un message dans " + conversation.getName())
                .read(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        pushToUser(recipient, saved);
    }

    @Transactional
    public void notifyAddedToConversation(User recipient, UUID conversationPublicId, String actorName) {
        Conversation conversation = conversationRepository.findByPublicId(conversationPublicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .conversation(conversation)
                .type(NotificationType.ADDED_TO_CONVERSATION)
                .content(actorName + " vous a ajouté à la conversation " + conversation.getName())
                .read(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        pushToUser(recipient, saved);
    }

    private void pushToUser(User recipient, Notification notification) {
        messagingTemplate.convertAndSendToUser(
                recipient.getEmail(),
                "/queue/notifications",
                notificationMapper.notificationToResponseDTO(notification)
        );
    }

    public Page<NotificationResponseDTO> getNotifications(UUID recipientPublicId, Pageable pageable) {
        return notificationRepository
                .findByRecipient_PublicIdOrderByCreatedAtDesc(recipientPublicId, pageable)
                .map(notificationMapper::notificationToResponseDTO);
    }

    public long getUnreadCount(UUID recipientPublicId) {
        return notificationRepository.countByRecipient_PublicIdAndReadFalse(recipientPublicId);
    }

    @Transactional
    public NotificationResponseDTO markAsRead(UUID notificationPublicId, UUID requesterPublicId) {
        Notification notification = notificationRepository.findByPublicId(notificationPublicId)
                .orElseThrow(() -> new NotFoundException("Notification non trouvée."));

        if (!notification.getRecipient().getPublicId().equals(requesterPublicId)) {
            throw new AccessDeniedException("Cette notification ne vous appartient pas.");
        }

        notification.setRead(true);
        notification.setReadAt(java.time.Instant.now());
        return notificationMapper.notificationToResponseDTO(notificationRepository.save(notification));
    }

    @Transactional
    public void markAllAsRead(UUID recipientPublicId) {
        notificationRepository.markAllAsRead(recipientPublicId);
    }
}
