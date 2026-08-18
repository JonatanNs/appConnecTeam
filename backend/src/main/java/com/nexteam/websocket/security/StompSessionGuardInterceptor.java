package com.nexteam.websocket.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Class 'StompSessionGuardInterceptor' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 19/08/2026 00:00
 */
@Component
@Slf4j
public class StompSessionGuardInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())
                || StompCommand.SEND.equals(accessor.getCommand())) {
            if (accessor.getUser() == null) {
                log.warn("Frame {} rejetée : session STOMP non authentifiée.", accessor.getCommand());
                throw new AuthenticationCredentialsNotFoundException("Session non authentifiée");
            }
        }

        return message;
    }
}
