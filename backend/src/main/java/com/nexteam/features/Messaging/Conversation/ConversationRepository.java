package com.nexteam.features.Messaging.Conversation;

import com.nexteam.features.Messaging.Message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Page<Conversation> findByUsersPublicId(UUID publicId, Pageable pageable);

    Optional<Conversation> findByPublicId(UUID publicId);

    Page<Conversation> findByNameContainingIgnoreCase(String word, Pageable pageable);

    @Query("""
        SELECT m
        FROM Message m
        WHERE m.conversation.publicId = :conversationId
          AND m.createdAt < :before
        ORDER BY m.createdAt DESC
    """)
    List<Message> findMessagesBefore(
            @Param("conversationId") UUID conversationId,
            @Param("before") Instant before,
            Pageable pageable
    );
}