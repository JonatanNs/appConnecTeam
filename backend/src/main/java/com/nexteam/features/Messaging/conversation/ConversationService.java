package com.nexteam.features.Messaging.conversation;

import com.nexteam.exceptions.AlreadyExistException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Messaging.conversation.dtos.ConvResponseDTO;
import com.nexteam.features.Messaging.conversation.dtos.mapper.ConvMapper;
import com.nexteam.features.Messaging.message.Message;
import com.nexteam.features.Messaging.message.MessageRepository;
import com.nexteam.features.Messaging.message.dtos.mapper.MessageMapper;
import com.nexteam.features.Messaging.message.enums.MessageType;
import com.nexteam.features.Messaging.notification.NotificationRepository;
import com.nexteam.features.Messaging.notification.NotificationService;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository convRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConvMapper convMapper;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    private ConvResponseDTO toResponseDTOFor(Conversation conversation, UUID currentUserPublicId) {
        ConvResponseDTO dto = convMapper.convToResponseDTO(conversation);

        if (conversation.getName() == null && conversation.getUsers().size() == 2) {
            String displayName = conversation.getUsers().stream()
                    .filter(u -> !u.getPublicId().equals(currentUserPublicId))
                    .findFirst()
                    .map(u -> u.getFirstname() + " " + u.getLastname())
                    .orElse("Conversation");
            dto.setName(displayName);
        }

        return dto;
    }

    public ConvResponseDTO getByPublicId(UUID publicId, UUID currentUserPublicId) {
        return toResponseDTOFor(
                convRepository.findByPublicId(publicId)
                        .orElseThrow(() -> new NotFoundException("Aucune conversation trouvée.")),
                currentUserPublicId
        );
    }

    public Page<ConvResponseDTO> getByNameContaining(String word, String userEmail, Pageable pageable, UUID currentUserPublicId) {
        Page<Conversation> conversations = convRepository.findByNameContainingIgnoreCaseAndUser(word, userEmail, pageable);
        return conversations.map(conv -> toResponseDTOFor(conv, currentUserPublicId));
    }

    public Page<ConvResponseDTO> getByUsersPublicId(UUID userPublicId, Pageable pageable) {
        return convRepository.findByUsersPublicId(userPublicId, pageable)
                .map(conv -> toResponseDTOFor(conv, userPublicId));
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
                return toResponseDTOFor(existing.get(), creator.getPublicId());
            }

            conversationName = null; // calculé dynamiquement selon qui regarde
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

        return toResponseDTOFor(saved, creator.getPublicId());
    }

    @Transactional
    public ConvResponseDTO updateConversation(UUID publicId, ConvRequestDTO requestDTO, UUID currentUserPublicId) {

        Conversation conversation = convRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        if (requestDTO.getName() != null && !requestDTO.getName().isBlank()
                && (conversation.getName() == null || !conversation.getName().equalsIgnoreCase(requestDTO.getName()))) {

            if (convRepository.existsByNameIgnoreCaseAndOwner_PublicId(
                    requestDTO.getName(), conversation.getOwner().getPublicId())) {
                throw new AlreadyExistException("Vous avez déjà une conversation portant ce nom.");
            }

            conversation.setName(requestDTO.getName());
        }

        if (requestDTO.getUsersIds() != null && !requestDTO.getUsersIds().isEmpty()) {

            Set<UUID> existingIds = conversation.getUsers().stream()
                    .map(User::getPublicId)
                    .collect(Collectors.toSet());

            List<UUID> newIds = requestDTO.getUsersIds().stream()
                    .filter(id -> !existingIds.contains(id))
                    .distinct()
                    .toList();

            if (!newIds.isEmpty()) {
                List<User> newUsers = userRepository.findAllByPublicIdIn(newIds);
                if (newUsers.size() != newIds.size()) {
                    throw new NotFoundException("Un ou plusieurs participants sont introuvables.");
                }

                conversation.getUsers().addAll(newUsers);

                String actorName = conversation.getOwner().getFirstname() + " " + conversation.getOwner().getLastname();
                newUsers.forEach(u ->
                        notificationService.notifyAddedToConversation(u, conversation.getPublicId(), actorName));
            }
        }

        return toResponseDTOFor(convRepository.save(conversation), currentUserPublicId);
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
            notificationRepository.deleteByConversation_PublicId(conversationId);
            convRepository.delete(conversation);
            return;
        }

        if (conversation.getOwner().getPublicId().equals(user.getPublicId())) {
            User newOwner = conversation.getUsers().iterator().next();
            conversation.setOwner(newOwner);
        }

        Conversation saved = convRepository.save(conversation);

        Message systemMessage = Message.builder()
                .conversation(saved)
                .sender(user)
                .content(user.getFirstname() + " a quitté la conversation")
                .type(MessageType.SYSTEM)
                .build();

        Message savedMessage = messageRepository.save(systemMessage);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversationId,
                messageMapper.messageToResponseDTO(savedMessage)
        );
    }

    @Transactional
    public void deleteConversation(UUID publicId) {
        convRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Conversation non trouvée."));

        notificationRepository.deleteByConversation_PublicId(publicId);
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
