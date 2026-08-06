package com.nexteam.exceptions;

/**
 * Classe 'NotFoundException' en charge de la gestion des exceptions pour les éléments non trouvés.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */

public class NotFoundException extends RuntimeException {
    /**
     * Constructeur de l'exception 'NotFoundException'.
     * @param message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
