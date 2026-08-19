package com.nexteam.features.Messaging.conversation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @EntityGraph(attributePaths = "users")
    Optional<Conversation> findByPublicId(UUID publicId);

    @EntityGraph(attributePaths = "users")
    Page<Conversation> findByNameContainingIgnoreCase(String word, Pageable pageable);

    Page<Conversation> findByUsersPublicId(UUID publicId, Pageable pageable);

    boolean existsByPublicIdAndUsers_Email(UUID publicId, String email);

    boolean existsByPublicIdAndOwner_Email(UUID publicId, String email);

    void deleteByPublicId(UUID publicId);

    boolean existsByNameIgnoreCase(String name);
}