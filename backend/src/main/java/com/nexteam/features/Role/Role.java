package com.nexteam.features.Role;

import com.nexteam.features.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Classe 'Role' en charge de représenter les différents roles d'un utilisateur.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
public class Role extends BaseEntity {

    @Column(length = 100, unique = true)
    @NotBlank(message = "Le nom du rôle ne peut pas être vide.")
    @Size(max = 100, message = "Le nom du rôle ne peut pas dépasser {max} caractères.")
    private String name;
}
