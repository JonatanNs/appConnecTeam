package com.nexteam.exceptions;

/**
 * Class 'InvalidCredentialsException' en charge de gérer les erreurs liées à des identifiants de connexion invalides.
 *
 * @author JonatanNs
 * @version 1.0
 * @since 09/08/2026 22:57
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
