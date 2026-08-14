package com.nexteam.common;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.UUID;

/**
 * Classe 'AuditableEntity' en charge de fournir les champs techniques communs
 * aux entités métier nécessitant un suivi d'audit.
 * Hérite de {@link BaseEntity} pour l'identifiant technique interne (id).
 *
 * @see BaseEntity
 *
 * @author jnsualu2026
 * @version 1.0
 * @since 19/06/2026 11:17
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditableEntity extends BaseEntity{
    @Version
    private int version;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(updatable = false, nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    /**
     * Génère un identifiant public unique et définit la date de création.
     */
    @PrePersist
    public void prePersist() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }
}
