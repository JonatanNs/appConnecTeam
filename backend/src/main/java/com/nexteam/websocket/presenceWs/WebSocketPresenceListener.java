package com.nexteam.websocket.presenceWs;

import com.nexteam.features.Users.User.UserRepository;
import com.nexteam.websocket.messagingWs.conversationWs.ConversationPresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * Class 'WebSocketPresenceListener' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 22:55
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketPresenceListener {

    private final ConversationPresenceService presenceService;
    private final UserRepository userRepository;

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {

        Principal principal = event.getUser();

        if (principal == null) {
            log.warn("Connexion WebSocket sans utilisateur authentifié.");
            return;
        }

        String email = principal.getName();

        log.info("Utilisateur connecté en WebSocket : {}", email);

        userRepository.findByEmail(email).ifPresent(user -> {

            user.setOnline(true);
            userRepository.save(user);

            log.info("{} est maintenant en ligne.", email);
        });
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = accessor.getUser();

        if (principal == null) {
            log.warn("Déconnexion WebSocket sans utilisateur authentifié.");
            return;
        }

        String email = principal.getName();

        log.info("Utilisateur déconnecté du WebSocket : {}", email);

        // Nettoyage de la présence dans les conversations
        presenceService.clearAllForUser(email);

        // Mise à jour du statut global
        userRepository.findByEmail(email).ifPresent(user -> {

            user.setOnline(false);
            userRepository.save(user);

            log.info("{} est maintenant hors ligne.", email);
        });
    }
}
