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
     * Crée une instance d' {@code ApiResponse} avec le code HTTP,
     * le message et les données spécifiés.
     *
     * <p>Le timestamp est automatiquement initialisé avec l'instant courant.</p>
     *
     * @param <T> type des données contenues dans la réponse
     * @param code code de la réponse
     * @param message message associé à la réponse
     * @param data données retournées dans la réponse, pouvant être {@code null}
     * @return une instance d' {@code ApiResponse} avec le timestamp courant
     */
    public static <T> ApiResponse<T> of(int code, String message, T data) {
        return new ApiResponse<>(code, Instant.now(), message, data);
    }

}
