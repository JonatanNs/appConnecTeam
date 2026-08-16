package com.nexteam.features.Users.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNameIgnoreCase(String name);
    Optional<Role> findByPublicId(UUID publicId);
    void deleteByPublicId(UUID publicId);
}
