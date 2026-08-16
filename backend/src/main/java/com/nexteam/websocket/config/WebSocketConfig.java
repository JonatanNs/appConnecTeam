package com.nexteam.websocket.config;

import com.nexteam.websocket.security.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Class 'WebSocketConfig' en charge de la configuration du support WebSocket/STOMP
 * de l'application, incluant l'enregistrement du point de connexion, la configuration
 * du broker de messages et la sécurisation des connexions entrantes via JWT.
 *
 * @author JonatanNs
 * @version 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    /**
     * le point d'entrée de connexion, une seule fois.
     * C'est l'URL que le client (Angular) utilise pour établir la connexion WebSocket initiale.
     * Une fois connecté, le client n'utilise plus jamais /ws directement — c'est juste la "porte d'entrée".
     *
     * @param registry le registre des endpoints STOMP à configurer
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200");
//                .withSockJS();
    }

    /**
     * Configure le broker de messages :
     * /topic
     * préfixe pour ce que le SERVEUR diffuse VERS les clients abonnés. Une fois que le serveur a traité le message reçu sur /app
     * /app
     * préfixe pour ce que le CLIENT envoie VERS le serveur. Quand Angular veut envoyer un message, il publie sur une destination commençant par /app/...
     *
     * @param registry le registre du broker de messages à configurer
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Configure le canal des messages entrants en y ajoutant l'intercepteur JWT,
     * chargé de vérifier et d'authentifier le token fourni par le client lors
     * de l'établissement de la connexion WebSocket.
     *
     * @param registration l'enregistrement du canal entrant à configurer
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor); // vérifie le token à la connexion
    }
}
