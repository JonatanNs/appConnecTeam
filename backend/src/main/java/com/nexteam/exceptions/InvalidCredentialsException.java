package com.nexteam.exceptions;

/**
 * Class 'BadCredentialsException' en charge de
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
