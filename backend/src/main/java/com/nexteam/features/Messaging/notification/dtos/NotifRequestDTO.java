package com.nexteam.features.Messaging.notification.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifRequestDTO {
    @NotBlank(message = "Le contenu du message ne peut pas être vide.")
    private String content;
    private Instant createdAt;
    private UUID sender;

}
