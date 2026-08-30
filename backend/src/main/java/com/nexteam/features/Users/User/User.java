package com.nexteam.features.Users.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexteam.common.AuditableEntity;
import com.nexteam.features.Messaging.message.Message;
import com.nexteam.features.Users.Address.Address;
import com.nexteam.features.Users.Role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * La classe User représente un utilisateur de l’application.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "persons")
public class User extends AuditableEntity {

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Ce champ ne peut pas être vide.")
    private String firstname;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Ce champ ne peut pas être vide.")
    private String lastname;

    @Email
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    //    @Pattern(
//            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
//            message = "Le mot de passe doit contenir majuscule, minuscule et chiffre."
    // )
    @Column(nullable = false)
    // TODO : mettre un mot de passe fort
    private String password;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean online;

    /**
     * Relation ManyToMany entre User et Role.
     * Un utilisateur peut avoir plusieurs rôles et un rôle peut être attribué à plusieurs utilisateurs
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
        name = "person_roles",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    /**
     * Relation ManyToOne entre User et Address.
     * Un utilisateur peut avoir une seule adresse et une adresse peut être attribuée à plusieurs utilisateurs.
     */
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Message> messages = new ArrayList<>();

    @Transient
    private String fullName = firstname + " " + lastname;
}



