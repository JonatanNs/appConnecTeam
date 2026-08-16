package com.nexteam.websocket.security;

import com.nexteam.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Class 'JwtChannelInterceptor' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 19:23
 */
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        System.out.println("===== STOMP =====");
        System.out.println("Command : " + accessor.getCommand());
        System.out.println("Destination : " + accessor.getDestination());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = accessor.getFirstNativeHeader("Authorization");

            System.out.println("Authorization : " + token);

            if (token != null && token.startsWith("Bearer ")) {

                String jwt = token.substring(7);
                String username = jwtService.extractUsername(jwt);

                System.out.println("Username : " + username);

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    var authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    accessor.setUser(authentication);

                    System.out.println("✅ Utilisateur authentifié : "
                            + authentication.getName());
                }
            }
        }

        return message;
    }

//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            String token = accessor.getFirstNativeHeader("Authorization");
//
//            if (token != null && token.startsWith("Bearer ")) {
//                String jwt = token.substring(7);
//                String username = jwtService.extractUsername(jwt);
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                if (jwtService.isTokenValid(jwt, userDetails)) {
//                    var authentication = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//                    accessor.setUser(authentication);
//                }
//            }
//        }
//        return message;
//    }
}
