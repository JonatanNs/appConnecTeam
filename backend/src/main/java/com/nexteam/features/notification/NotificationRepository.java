package com.nexteam.features.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * Class 'NotificationRepository' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 22:14
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByPublicId(UUID publicId);

    Page<Notification> findByRecipient_PublicIdOrderByCreatedAtDesc(UUID recipientPublicId, Pageable pageable);

    long countByRecipient_PublicIdAndReadFalse(UUID recipientPublicId);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true, n.readAt = CURRENT_TIMESTAMP " +
            "WHERE n.recipient.publicId = :recipientPublicId AND n.read = false")
    void markAllAsRead(UUID recipientPublicId);
}
