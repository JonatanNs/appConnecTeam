package com.nexteam.websocket.security;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * Class 'CustomHandshakeHandler' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 18/08/2026 23:59
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        Object principal = attributes.get("principal");
        return (principal instanceof Principal) ? (Principal) principal : null;
    }
}
