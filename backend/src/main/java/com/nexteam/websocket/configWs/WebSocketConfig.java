package com.nexteam.websocket.configWs;

import com.nexteam.websocket.exceptionWs.CustomStompErrorHandler;
import com.nexteam.websocket.securityWs.CustomHandshakeHandler;
import com.nexteam.websocket.securityWs.JwtHandshakeInterceptor;
import com.nexteam.websocket.securityWs.StompSessionGuardInterceptor;
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

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final StompSessionGuardInterceptor stompSessionGuardInterceptor;
    private final CustomStompErrorHandler customStompErrorHandler;

    // TODO: remplacer le hardcode par une valeur configurable (application.yml / variable d'env)
    //       avant le passage en prod, pour ne pas coder en dur le domaine front.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:4200", "http://192.168.*.*:4200", "http://10.*.*.*:4200", "http://169.254.*.*:4200")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(new CustomHandshakeHandler());

        registry.setErrorHandler(customStompErrorHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompSessionGuardInterceptor);
    }
}
