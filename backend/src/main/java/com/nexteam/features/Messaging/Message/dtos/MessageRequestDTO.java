package com.nexteam.features.Messaging.Message.dtos;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    private List<String> content;
    private Instant updatedAt;
}
