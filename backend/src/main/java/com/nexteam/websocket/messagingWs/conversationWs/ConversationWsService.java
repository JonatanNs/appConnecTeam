package com.nexteam.websocket.messagingWs.conversationWs;

import com.nexteam.features.Messaging.conversation.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final ConversationRepository convRepository;

    public void assertParticipant(UUID conversationId, String userEmail) {
        boolean isParticipant = convRepository.existsByPublicIdAndUsers_Email(conversationId, userEmail);
        if (!isParticipant) {
            throw new AccessDeniedException("Vous n'êtes pas participant de cette conversation.");
        }
    }

    public void assertOwner(UUID conversationId, String userEmail) {
        boolean isOwner = convRepository.existsByPublicIdAndOwner_Email(conversationId, userEmail);
        if (!isOwner) {
            throw new AccessDeniedException("Seul le créateur de la conversation peut effectuer cette action.");
        }
    }
}
