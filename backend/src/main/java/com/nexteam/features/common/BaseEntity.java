package com.nexteam.features.common;

import jakarta.persistence.*;
import lombok.*;

/**
 * Classe 'BaseEntity' en charge de fournir l'identifiant technique commun
 * à toutes les entités JPA de l'application, généré automatiquement par
 * la base de données.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}

