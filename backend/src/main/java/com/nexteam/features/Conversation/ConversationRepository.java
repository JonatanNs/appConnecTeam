package com.nexteam.features.Conversation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @EntityGraph(attributePaths = "users")
    Optional<Conversation> findByPublicId(UUID publicId);

    List<Conversation> findByNameContainingIgnoreCase(String word);

    Page<Conversation> findByUsersPublicId(UUID publicId, Pageable pageable);

    boolean existsByPublicIdAndUsers_Email(UUID publicId, String email);

    boolean existsByPublicIdAndOwner_Email(UUID publicId, String email);

    void deleteByPublicId(UUID publicId);
}