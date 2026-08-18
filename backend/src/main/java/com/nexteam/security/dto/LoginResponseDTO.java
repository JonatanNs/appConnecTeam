package com.nexteam.security.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private UUID publicId;
    private String email;
    private String firstname;
    private String lastname;
}
