package com.nexteam.features.Users.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe 'UserRepository' en charge de la gestion des opérations CRUD sur les utilisateurs.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles", "address"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "address"})
    Optional<User> findByPublicId(UUID publicId);

    void deleteByPublicId(UUID publicId);

    Page<User> findByRoles_NameIgnoreCase(String roleName, Pageable pageable);

    @Query("""
            SELECT u FROM User u
            WHERE :name IS NULL
               OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :name, '%'))
               OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<User> search(
            @Param("name") String name,
            Pageable pageable
    );

    List<User> findAllByPublicIdIn(Collection<UUID> publicIds);
}
