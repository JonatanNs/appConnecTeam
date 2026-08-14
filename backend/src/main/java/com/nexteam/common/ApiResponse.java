package com.nexteam.common;

import java.time.Instant;

/**
 * Classe 'ApiResponse' en charge de la gestion des réponses d'API.
 *
 * @author jnsualu2026
 * @version 1.0
 * @since 19/06/2026 14:15
 */
public record ApiResponse<T>(int code, Instant timestamp , String message, T data) {

    /**
     * Crée une instance d'ApiResponse avec le code, le message et les données spécifiés.
     * @param <T>
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static <T> ApiResponse<T> of(int code, String message, T data) {
        return new ApiResponse<>(code, Instant.now(), message, data);
    }

}
