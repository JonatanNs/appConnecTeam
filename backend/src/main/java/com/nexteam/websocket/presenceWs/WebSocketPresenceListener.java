package com.nexteam.websocket.presenceWs;

import com.nexteam.websocket.messagingWs.conversationWs.ConversationPresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Class 'WebSocketPresenceListener' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 22:55
 */
@Component
@RequiredArgsConstructor
public class WebSocketPresenceListener {

    private final ConversationPresenceService presenceService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null) {
            presenceService.clearAllForUser(accessor.getUser().getName());
        }
    }
}
