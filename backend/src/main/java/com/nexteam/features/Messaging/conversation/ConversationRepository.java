package com.nexteam.features.Messaging.conversation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @EntityGraph(attributePaths = "users")
    Optional<Conversation> findByPublicId(UUID publicId);

    @EntityGraph(attributePaths = "users")
    @Query("""
        SELECT c FROM Conversation c
        JOIN c.users u
        WHERE u.email = :userEmail
          AND LOWER(c.name) LIKE LOWER(CONCAT('%', :word, '%'))
        """)
    Page<Conversation> findByNameContainingIgnoreCaseAndUser(
            @Param("word") String word,
            @Param("userEmail") String userEmail,
            Pageable pageable
    );

    Page<Conversation> findByUsersPublicId(UUID publicId, Pageable pageable);

    boolean existsByPublicIdAndUsers_Email(UUID publicId, String email);

    boolean existsByPublicIdAndOwner_Email(UUID publicId, String email);

    void deleteByPublicId(UUID publicId);

    boolean existsByNameIgnoreCaseAndOwner_PublicId(String name, UUID ownerPublicId);

    @Query("""
            SELECT c FROM Conversation c
            JOIN c.users u
            WHERE u.publicId IN (:userAId, :userBId)
            GROUP BY c
            HAVING COUNT(DISTINCT u.publicId) = 2
               AND (SELECT COUNT(u2) FROM c.users u2) = 2
            """)
    Optional<Conversation> findOneToOneConversation(@Param("userAId") UUID userAId, @Param("userBId") UUID userBId);
}