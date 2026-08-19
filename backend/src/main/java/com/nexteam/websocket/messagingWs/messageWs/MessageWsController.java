package com.nexteam.websocket.messagingWs.messageWs;

import com.nexteam.features.Users.User.UserService;
import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import com.nexteam.features.Messaging.message.dtos.MessageRequestDTO;
import com.nexteam.features.Messaging.message.dtos.MessageResponseDTO;
import com.nexteam.websocket.messagingWs.messageWs.dtosWs.TypingEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

/**
 * Class 'MessageController' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 22:30
 */
@Controller
@RequiredArgsConstructor
public class MessageWsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageWsService messageWsService;
    private final UserService userService;

    @MessageMapping("/conversations/{conversationId}/send")
    public void sendMessage(@DestinationVariable UUID conversationId,
                            @Payload MessageRequestDTO dto,
                            Principal principal) {
        MessageResponseDTO message = messageWsService.sendMessage(conversationId, dto, principal.getName());
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, message);
    }

    @MessageMapping("/conversations/{conversationId}/typing")
    public void typing(@DestinationVariable UUID conversationId,
                       @Payload TypingEventDTO event,
                       Principal principal) {

        UserResponseDTO sender = userService.getUserByEmail(principal.getName());

        TypingEventDTO enrichedEvent = TypingEventDTO.builder()
                .conversationId(conversationId)
                .userId(sender.getPublicId())
                .userName(sender.getFullname())
                .isTyping(event.isTyping())
                .build();

        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId + "/typing", enrichedEvent);
    }
}
