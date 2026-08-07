package com.nexteam.features.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Classe 'UserRepository' en charge de la gestion des opérations CRUD sur les utilisateurs.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPublicId(UUID publicId);
    void deleteByPublicId(UUID publicId);
    Page<User> findByRoles_NameIgnoreCase(String roleName, Pageable pageable);

    @Query("""
            SELECT u FROM User u
            WHERE (:firstname IS NULL OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :firstname, '%')))
            AND (:lastname IS NULL OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :lastname, '%')))
            """)
    Page<User> search(
            @Param("firstname") String firstname,
            @Param("lastname") String lastname,
            Pageable pageable
    );
}
