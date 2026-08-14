package com.nexteam.features.Messaging.Conversation;

import com.nexteam.features.Messaging.Message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Page<Conversation> findByUserPublicId(UUID publicId, Pageable pageable);

    Optional<Conversation> findByPublicId(UUID publicId, Pageable pageable);

    Page<Conversation> findByNameContainingIgnoreCase(String word, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.publicId = :conversationId AND m.createdAt < :before ORDER BY m.createdAt DESC")
    List<Message> findMessagesBefore(UUID conversationId, Instant before, Pageable pageable);
}
