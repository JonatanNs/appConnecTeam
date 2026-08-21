package com.nexteam.websocket.messagingWs.conversationWs;

import com.nexteam.features.Messaging.conversation.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    private final ConversationService conversationService;
    private final ConversationPresenceService presenceService;

    @MessageMapping("/conversations/{conversationId}/join")
    public void join(@DestinationVariable UUID conversationId, Principal principal) {
        conversationService.assertParticipant(conversationId, principal.getName());
        presenceService.markOpen(principal.getName(), conversationId.toString());
    }

    @MessageMapping("/conversations/{conversationId}/leave")
    public void leave(@DestinationVariable UUID conversationId, Principal principal) {
        conversationService.assertParticipant(conversationId, principal.getName());
        presenceService.markClosed(principal.getName(), conversationId.toString());
    }
}
