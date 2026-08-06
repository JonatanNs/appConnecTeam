package com.nexteam.features.Address;

import com.nexteam.features.common.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Classe 'Address' en charge de représenter l'adresse d'un utilisateur.
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
public class Address extends AuditableEntity {

    @Column(nullable = false)
    @NotBlank(message = "La rue ne peut pas être vide.")
    private String street;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Le nom de la ville ne peut pas être vide.")
    @Size(max = 100, message = "Le nom de la ville ne peut pas dépasser {max} caractères.")
    private String city;

    @Column(length = 10, nullable = false)
    @NotBlank(message = "Le code postal ne peut pas être vide.")
    @Size(max = 10, message = "Le code postal ne peut pas dépasser {max} caractères.")
    private String zipcode;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Le nom du pays ne peut pas être vide.")
    @Size(max = 100, message = "Le nom du pays ne peut pas dépasser {max} caractères.")
    private String country;
}
