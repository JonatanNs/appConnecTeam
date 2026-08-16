package com.nexteam.features.Messaging.Message.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    @NotBlank(message = "Le contenu du message ne peut pas être vide.")
    private String content;
    private Instant createdAt;

}
