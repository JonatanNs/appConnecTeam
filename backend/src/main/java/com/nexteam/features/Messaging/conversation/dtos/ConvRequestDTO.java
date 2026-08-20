package com.nexteam.features.Messaging.conversation.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvRequestDTO {
    private Set<UUID> usersIds;
    @Size(max = 255, message = "Le nom ne peut pas dépasser {max} caractères.")
    private String name;
}
