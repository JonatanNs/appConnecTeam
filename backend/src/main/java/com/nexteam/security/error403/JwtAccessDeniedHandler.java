package com.nexteam.security.error403;

import com.nexteam.features.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);
    private final ObjectMapper mapper;


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String message;

        if (auth != null) {
            Object principal = auth.getPrincipal();
            String username = principal instanceof UserDetails userDetails
                    ? userDetails.getUsername()
                    : principal.toString();

            logger.warn("Accès refusé - utilisateur : {} - URL : {}", username, request.getRequestURI());
            message = "Vous n'avez pas les droits pour accéder à cette ressource";
        } else {
            message = "Accès refusé";
        }

        ApiResponse<?> error = ApiResponse.of(
                HttpStatus.FORBIDDEN.value(),
                message,
                null
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        mapper.writeValue(response.getOutputStream(), error);
    }
}
