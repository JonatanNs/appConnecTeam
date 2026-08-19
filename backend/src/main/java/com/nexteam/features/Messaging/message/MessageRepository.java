package com.nexteam.features.Messaging.message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findByPublicId(UUID publicId);

    /**
     * Récupère les messages d'une conversation, paginés par curseur temporel.
     * Retourne les messages envoyés avant l'instant donné, triés du plus récent au plus ancien,
     * dans la limite fixée par le Pageable — utilisé pour le chargement initial d'une conversation
     * et le défilement vers l'historique (scroll infini).
     *
     * @param conversationId l'identifiant public de la conversation concernée
     * @param before         l'instant à partir duquel chercher les messages précédents (exclusif)
     * @param pageable       pagination définissant le nombre maximal de messages à retourner
     * @return la liste des messages précédant l'instant donné, du plus récent au plus ancien
     */
    @Query("""
                SELECT m
                FROM Message m
                WHERE m.conversation.publicId = :conversationId
                  AND m.createdAt < :before
                ORDER BY m.createdAt ASC
            """)
    List<Message> findMessagesBefore(
            @Param("conversationId") UUID conversationId,
            @Param("before") Instant before,
            Pageable pageable
    );
}
