package com.nexteam.exceptions;

/**
 * Classe 'AlreadyExisteException' en charge de la gestion des exceptions pour les éléments déjà existants.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
public class AlreadyExistException extends RuntimeException {
    /**
     * Constructeur de l'exception 'AlreadyExistException'.
     * @param message
     */
    public AlreadyExistException(String message) {
        super(message);
    }
}
