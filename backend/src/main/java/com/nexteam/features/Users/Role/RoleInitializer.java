package com.nexteam.features.Users.Role;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private static final List<String> DEFAULT_ROLES = List.of(
            "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"
    );
    private final RoleRepository roleRepository;

    @Override
    public void run(String @NonNull ... args) {
        DEFAULT_ROLES.forEach(roleName -> {
            if (roleRepository.findByNameIgnoreCase(roleName).isEmpty()) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();
                roleRepository.save(role);
            }
        });
    }
}
