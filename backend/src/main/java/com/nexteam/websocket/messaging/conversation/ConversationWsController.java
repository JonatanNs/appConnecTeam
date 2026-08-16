package com.nexteam.websocket.messaging.conversation;

import com.nexteam.features.Messaging.Message.dtos.MessageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

/**
 * Class 'ConversationWsController' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 22:47
 */
@Controller
@RequiredArgsConstructor
public class ConversationWsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationWsService wsService;

    //    @MessageMapping("/conversations/{conversationId}/join")
//    public void join(@DestinationVariable UUID conversationId, Principal principal) {
//        MessageResponseDTO systemMessage = wsService.createSystemMessage(
//                conversationId, principal.getName(), "a rejoint la conversation");
//        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, systemMessage);
//    }
    @MessageMapping("/conversations/{conversationId}/join")
    public void join(@DestinationVariable UUID conversationId, Principal principal) {

        System.out.println("========== JOIN ==========");
        System.out.println("Conversation : " + conversationId);
        System.out.println("Principal : " + principal);

        MessageResponseDTO systemMessage = wsService.createSystemMessage(
                conversationId,
                principal.getName(),
                "a rejoint la conversation"
        );

        System.out.println("Message créé : " + systemMessage);

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversationId,
                systemMessage
        );

        System.out.println("Message envoyé !");
    }

    @MessageMapping("/conversations/{conversationId}/leave")
    public void leave(@DestinationVariable UUID conversationId, Principal principal) {
        MessageResponseDTO systemMessage = wsService.createSystemMessage(
                conversationId, principal.getName(), "a quitté la conversation");
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, systemMessage);
    }
}
