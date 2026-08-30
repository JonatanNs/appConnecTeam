package com.nexteam.security.dto;

import lombok.*;

import java.util.UUID;

/**
 * Classe 'LoginResponseDTO' en charge d'exposer les informations nécessaires au client après la connexion sans
 *  exposer directement l'entité 'User'.
 *
 * @author jnsualu2026
 * @since 2026-08-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String refreshToken;
    private UUID publicId;
    private String email;
    private String firstname;
    private String lastname;
    private Boolean online;

}
