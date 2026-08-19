package com.nexteam.features.Messaging.conversation.dtos;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Le nom de la conversation ne peut pas être vide.")
    @Size(max = 255, message = "Le nom ne peut pas dépasser {max} caractères.")
    private String name;
}
