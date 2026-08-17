package com.nexteam.websocket.messaging.message;

import com.nexteam.features.Messaging.Message.dtos.MessageRequestDTO;
import com.nexteam.features.Messaging.Message.dtos.MessageResponseDTO;
import com.nexteam.websocket.messaging.message.dtos.TypingEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

/**
 * Class 'MessageWsController' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 22:30
 */
@Controller
@RequiredArgsConstructor
public class MessageWsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageWsService messageService;

    @MessageMapping("/conversations/{conversationId}/send")
    public void sendMessage(@DestinationVariable UUID conversationId,
                            @Payload MessageRequestDTO dto,
                            Principal principal) {
        MessageResponseDTO message = messageService.sendMessage(conversationId, dto, principal.getName());
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, message);
    }

    @MessageMapping("/conversations/{conversationId}/typing")
    public void typing(@DestinationVariable UUID conversationId, TypingEventDTO event) {
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId + "/typing", event);
    }
}
