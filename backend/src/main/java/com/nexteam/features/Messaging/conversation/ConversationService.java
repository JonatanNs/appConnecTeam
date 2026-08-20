package com.nexteam.features.Messaging.conversation;

import com.nexteam.exceptions.AlreadyExistException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Messaging.conversation.dtos.ConvResponseDTO;
import com.nexteam.features.Messaging.conversation.dtos.mapper.ConvMapper;
import com.nexteam.features.Messaging.notification.NotificationService;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Méthode en charge de crée une conversation. Le créateur devient propriétaire et participant.
     * Pour une conversation à 2 participants, réutilise une conversation privée
     * existante entre les mêmes utilisateurs si elle existe (évite les doublons).
     *
     * @throws NotFoundException        si le créateur ou un participant est introuvable
     * @throws IllegalArgumentException si aucun participant n'est fourni
     */
    @Transactional
    public ConvResponseDTO createConversation(ConvRequestDTO requestDTO, String creatorEmail) {

        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        if (requestDTO.getUsersIds() == null || requestDTO.getUsersIds().isEmpty()) {
            throw new IllegalArgumentException("Sélectionnez au moins un participant.");
        }

        List<UUID> filteredIds = requestDTO.getUsersIds().stream()
                .filter(id -> !id.equals(creator.getPublicId()))
                .distinct()
                .toList();

        if (filteredIds.isEmpty()) {
            throw new IllegalArgumentException("Sélectionnez au moins un autre participant.");
        }

        List<User> foundUsers = userRepository.findAllByPublicIdIn(filteredIds);
        if (foundUsers.size() != filteredIds.size()) {
            throw new NotFoundException("Un ou plusieurs participants sont introuvables.");
        }

        Set<User> participants = new HashSet<>(foundUsers);
        participants.add(creator);

        String conversationName = requestDTO.getName();

        if (participants.size() == 2) {
            User otherUser = participants.stream()
                    .filter(u -> !u.getPublicId().equals(creator.getPublicId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Autre participant introuvable."));

            Optional<Conversation> existing =
                    convRepository.findOneToOneConversation(creator.getPublicId(), otherUser.getPublicId());

            if (existing.isPresent()) {
                return convMapper.convToResponseDTO(existing.get());
            }

            conversationName = otherUser.getFirstname() + " " + otherUser.getLastname();
        } else if (conversationName == null || conversationName.isBlank()) {
            conversationName = participants.stream()
                    .filter(u -> !u.getPublicId().equals(creator.getPublicId()))
                    .map(User::getFirstname)
                    .limit(3)
                    .collect(Collectors.joining(", "));
        }

        Conversation conversation = Conversation.builder()
                .name(conversationName)
                .owner(creator)
                .users(participants)
                .build();

        Conversation saved = convRepository.save(conversation);

        participants.stream()
                .filter(u -> !u.getPublicId().equals(creator.getPublicId()))
                .forEach(u ->
                        notificationService.notifyAddedToConversation(
                                u,
                                saved.getPublicId(),
                                creator.getFirstname() + " " + creator.getLastname()
                        )
                );

        return convMapper.convToResponseDTO(saved);
    }

    @Transactional
    public ConvResponseDTO updateConversation(UUID publicId, ConvRequestDTO requestDTO) {

        Conversation conversation = convRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        boolean nameChanged = !conversation.getName().equalsIgnoreCase(requestDTO.getName());

        if (nameChanged && convRepository.existsByNameIgnoreCaseAndOwner_PublicId(
                requestDTO.getName(), conversation.getOwner().getPublicId())) {
            throw new AlreadyExistException("Vous avez déjà une conversation portant ce nom.");
        }

        conversation.setName(requestDTO.getName());

        return convMapper.convToResponseDTO(convRepository.save(conversation));
    }

    @Transactional
    public void leaveConversation(UUID conversationId, String userEmail) {
        Conversation conversation = convRepository.findByPublicId(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        boolean isParticipant = conversation.getUsers().stream()
                .anyMatch(u -> u.getPublicId().equals(user.getPublicId()));
        if (!isParticipant) {
            throw new AccessDeniedException("Vous n'êtes pas participant de cette conversation.");
        }

        conversation.getUsers().remove(user);

        if (conversation.getUsers().isEmpty()) {
            convRepository.delete(conversation);
            return;
        }

        if (conversation.getOwner().getPublicId().equals(user.getPublicId())) {
            User newOwner = conversation.getUsers().iterator().next();
            conversation.setOwner(newOwner);
        }

        convRepository.save(conversation);
    }

    @Transactional
    public void deleteConversation(UUID publicId) {
        convRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));
        convRepository.deleteByPublicId(publicId);
    }
}
