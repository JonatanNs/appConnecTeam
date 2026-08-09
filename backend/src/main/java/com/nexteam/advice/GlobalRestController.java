package com.nexteam.advice;

import com.nexteam.exceptions.AlreadyExistException;
import com.nexteam.exceptions.BadCredentialsException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe 'GlobalRestController' en charge de la gestion globale des exceptions pour les contrôleurs REST.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestController{

    private final AuthenticationManager authenticateManager;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handlePathNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ApiResponse<?>> handleAlreadyExist(AlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage(), null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.of(HttpStatus.UNAUTHORIZED.value(), "Email ou mot de passe incorrect.", null));
    }
}
