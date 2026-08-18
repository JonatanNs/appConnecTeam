package com.nexteam.features.Conversation;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Conversation.dtos.ConvResponseDTO;
import com.nexteam.features.Conversation.dtos.mapper.ConvMapper;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import com.nexteam.features.notification.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository convRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ConvMapper convMapper;

    public ConvResponseDTO getByPublicId(UUID publicId) {
        return convMapper.convToResponseDTO(
                convRepository.findByPublicId(publicId)
                        .orElseThrow(() -> new NotFoundException("Aucune conversation trouvée."))
        );
    }

    public Page<ConvResponseDTO> getByNameContaining(String word, Pageable pageable) {
        Page<Conversation> conversations = convRepository.findByNameContainingIgnoreCase(word, pageable);
        return conversations.map(convMapper::convToResponseDTO);
    }

    public Page<ConvResponseDTO> getByUsersPublicId(UUID userPublicId, Pageable pageable) {
        return convRepository.findByUsersPublicId(userPublicId, pageable)
                .map(convMapper::convToResponseDTO);
    }

    @Transactional
    public ConvResponseDTO createConversation(ConvRequestDTO requestDTO, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        Set<User> participants = new HashSet<>(userRepository.findAllByPublicIdIn(requestDTO.getUsersIds()));
        participants.add(creator); // le créateur fait partie des participants

        Conversation conversation = Conversation.builder()
                .name(requestDTO.getName())
                .owner(creator)
                .users(participants)
                .build();

        Conversation saved = convRepository.save(conversation);

        // Notifier les participants ajoutés (hors créateur)
        participants.stream()
                .filter(u -> !u.getPublicId().equals(creator.getPublicId()))
                .forEach(u -> notificationService.notifyAddedToConversation(u, saved.getPublicId(), creator.getFirstname() + " " + creator.getLastname()));

        return convMapper.convToResponseDTO(saved);
    }

    @Transactional
    public ConvResponseDTO updateConversation(UUID publicId, ConvRequestDTO requestDTO) {
        Conversation conversation = convRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        conversation.setName(requestDTO.getName());

        return convMapper.convToResponseDTO(convRepository.save(conversation));
    }

    @Transactional
    public void deleteConversation(UUID publicId) {
        convRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));
        convRepository.deleteByPublicId(publicId);
    }

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
