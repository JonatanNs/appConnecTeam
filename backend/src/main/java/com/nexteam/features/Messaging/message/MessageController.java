package com.nexteam.features.Messaging.message;

import com.nexteam.common.ApiResponse;
import com.nexteam.features.Messaging.message.dtos.MessageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/conversations")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<ApiResponse<List<MessageResponseDTO>>> getMessages(
            @PathVariable UUID conversationId,
            @RequestParam(required = false) Instant before,
            @RequestParam(defaultValue = "20") int limit,
            @AuthenticationPrincipal UserDetails principal) {

        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Messages récupérés avec succès.",
                        messageService.getMessages(conversationId, before, limit, principal.getUsername()))
        );
    }
}
