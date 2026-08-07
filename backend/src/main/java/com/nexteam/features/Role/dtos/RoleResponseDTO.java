package com.nexteam.features.Role.dtos;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private UUID publicId;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
