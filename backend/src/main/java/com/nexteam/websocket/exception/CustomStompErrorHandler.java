package com.nexteam.websocket.exception;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

/**
 * Class 'CustomStompErrorHandler' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 21:31
 */
@Component
public class CustomStompErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

        String errorMessage = "Erreur interne du serveur";
        if (cause instanceof AuthenticationCredentialsNotFoundException || cause instanceof AccessDeniedException) {
            errorMessage = cause.getMessage();
        }

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(errorMessage);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }
}
