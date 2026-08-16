package com.nexteam.features.Messaging.Conversation;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Messaging.Conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Messaging.Conversation.dtos.ConvResponseDTO;
import com.nexteam.features.Messaging.Conversation.dtos.mapper.ConvMapper;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository convRepository;
    private final UserRepository userRepository;
    private final ConvMapper convMapper;

    public ConvResponseDTO getByPublicId(UUID publicId) {
        return convMapper.convToResponseDTO(
                convRepository.findByPublicId(publicId)
                        .orElseThrow(() -> new NotFoundException("Aucune conversation trouvée."))
        );
    }

    public List<ConvResponseDTO> getByNameContaining(String word) {
        List<Conversation> conversations = convRepository.findByNameContainingIgnoreCase(word);

        if (conversations.isEmpty()) {
            throw new NotFoundException("Aucune conversation trouvée.");
        }

        return conversations.stream()
                .map(convMapper::convToResponseDTO)
                .toList();
    }

    public Page<ConvResponseDTO> getByUsersPublicId(UUID userPublicId, Pageable pageable) {
        return convRepository.findByUsersPublicId(userPublicId, pageable)
                .map(convMapper::convToResponseDTO);
    }

    @Transactional
    public ConvResponseDTO createConversation(ConvRequestDTO requestDTO) {
        Set<User> users = new HashSet<>(userRepository.findAllByPublicIdIn(requestDTO.getUsersIds()));

        if (users.size() != requestDTO.getUsersIds().size()) {
            throw new NotFoundException("Un ou plusieurs utilisateurs sont introuvables.");
        }

        Conversation conversation = Conversation.builder()
                .name(requestDTO.getName())
                .users(users)
                .build();

        return convMapper.convToResponseDTO(convRepository.save(conversation));
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
}
