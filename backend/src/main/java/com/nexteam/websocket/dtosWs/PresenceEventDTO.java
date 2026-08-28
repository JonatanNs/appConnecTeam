package com.nexteam.websocket.dtosWs;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresenceEventDTO {
    private UUID userId;
    private boolean online;
}