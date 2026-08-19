package com.nexteam.features.Messaging.message;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.conversation.ConversationRepository;
import com.nexteam.features.Messaging.conversation.ConversationService;
import com.nexteam.features.Messaging.message.dtos.MessageResponseDTO;
import com.nexteam.features.Messaging.message.dtos.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ConversationRepository conversationRepository;
    private final ConversationService conversationService;

    public List<MessageResponseDTO> getMessages(UUID conversationId, Instant before, int limit, String requesterEmail) {
        conversationRepository.findByPublicId(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        conversationService.assertParticipant(conversationId, requesterEmail);

        Instant cursor = (before != null) ? before : Instant.now();
        Pageable pageable = PageRequest.of(0, limit);

        return messageRepository.findMessagesBefore(conversationId, cursor, pageable).stream()
                .map(messageMapper::messageToResponseDTO)
                .toList();
    }
}
