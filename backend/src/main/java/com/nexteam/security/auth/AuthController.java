package com.nexteam.security.auth;

import com.nexteam.common.ApiResponse;
import com.nexteam.features.Users.User.UserService;
import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import com.nexteam.security.dto.LoginRequestDTO;
import com.nexteam.security.dto.LoginResponseDTO;
import com.nexteam.security.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("access_token", response.getToken())
                .httpOnly(true)
                .secure(false)          // false en dev HTTP, true en prod HTTPS
                .sameSite("Lax")        // Lax suffit si tes requêtes sont bien du même "site" au sens large (localhost)
                .path("/")
                .maxAge(Duration.ofMillis(jwtService.getExpirationMs()))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.of(HttpStatus.OK.value(), "Connexion réussie.", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getCurrentUser(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            throw new AuthenticationCredentialsNotFoundException("Non authentifié.");
        }

        UserResponseDTO user = userService.getUserByEmail(principal.getUsername());

        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Utilisateur récupéré avec succès.", user)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        ResponseCookie expiredCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body(ApiResponse.of(HttpStatus.OK.value(), "Déconnexion réussie.", null));
    }
}
