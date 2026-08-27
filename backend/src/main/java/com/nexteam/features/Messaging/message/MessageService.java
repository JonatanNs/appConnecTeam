package com.nexteam.features.Messaging.message;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.conversation.ConversationRepository;
import com.nexteam.features.Messaging.conversation.ConversationService;
import com.nexteam.features.Messaging.message.dtos.MessageResponseDTO;
import com.nexteam.features.Messaging.message.dtos.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

    /**
     * Récupère l'historique des messages d'une conversation, paginé par curseur temporel.
     * <p>
     * Si {@code before} est {@code null}, la pagination démarre depuis l'instant présent
     * (les messages les plus récents). Sinon, elle récupère les messages envoyés avant
     * cet instant, permettant de charger l'historique progressivement (scroll infini).
     * <p>
     * Le résultat est retourné dans l'ordre chronologique (du plus ancien au plus récent),
     * prêt à être affiché directement dans un fil de discussion.
     *
     * @param conversationId identifiant public de la conversation
     * @param before         instant à partir duquel remonter dans l'historique ({@code null} pour partir du présent)
     * @param limit          nombre maximum de messages à récupérer
     * @param requesterEmail email de l'utilisateur effectuant la requête, utilisé pour vérifier qu'il participe à la conversation
     * @return la liste des messages, du plus ancien au plus récent
     * @throws NotFoundException     si la conversation n'existe pas
     * @throws AccessDeniedException si l'utilisateur ne participe pas à la conversation
     */
    public List<MessageResponseDTO> getMessages(UUID conversationId, Instant before, int limit, String requesterEmail) {
        conversationRepository.findByPublicId(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        conversationService.assertParticipant(conversationId, requesterEmail);

        Instant cursor = (before != null) ? before : Instant.now();
        Pageable pageable = PageRequest.of(0, limit);

        List<MessageResponseDTO> messages = messageRepository.findMessagesBefore(conversationId, cursor, pageable).stream()
                .map(messageMapper::messageToResponseDTO)
                .toList();

        return messages.reversed(); // ordre chronologique : plus ancien → plus récent
    }
}
