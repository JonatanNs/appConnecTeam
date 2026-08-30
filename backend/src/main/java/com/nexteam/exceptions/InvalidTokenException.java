package com.nexteam.exceptions;

/**
 * Class 'InvalidTokenException' en charge d'invalidé si le token est révoqué ou expiré
 *
 * @author JonatanNs
 * @version 1.0
 * @since 30/08/2026 14:36
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
