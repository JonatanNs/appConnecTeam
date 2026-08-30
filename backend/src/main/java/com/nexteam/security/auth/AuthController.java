package com.nexteam.security.auth;

import com.nexteam.common.ApiResponse;
import com.nexteam.features.Users.User.UserService;
import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import com.nexteam.security.dto.LoginRequestDTO;
import com.nexteam.security.dto.LoginResponseDTO;
import com.nexteam.security.jwt.JwtService;
import com.nexteam.security.refreshToken.TokenService;
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
    private final TokenService tokenService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return buildAuthResponse(response, "Connexion réussie.");
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

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken == null) {
            throw new AuthenticationCredentialsNotFoundException("Refresh token manquant.");
        }

        LoginResponseDTO response = authService.refresh(refreshToken);
        return buildAuthResponse(response, "Token renouvelé.");
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken != null) {
            tokenService.revokeToken(refreshToken);
        }

        ResponseCookie expiredAccessCookie = buildCookie("access_token", "", "/", 0);
        ResponseCookie expiredRefreshCookie = buildCookie("refresh_token", "", "/api/v1/auth", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAccessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, expiredRefreshCookie.toString())
                .body(ApiResponse.of(HttpStatus.OK.value(), "Déconnexion réussie.", null));
    }

    /**
     * Construit la réponse commune à login/refresh : pose les cookies access_token
     * et refresh_token, puis retire le refresh token du body avant de le retourner.
     */
    private ResponseEntity<ApiResponse<LoginResponseDTO>> buildAuthResponse(LoginResponseDTO response, String message) {
        ResponseCookie accessCookie = buildCookie(
                "access_token", response.getToken(), "/", jwtService.getExpirationMs() / 1000
        );

        ResponseCookie refreshCookie = buildCookie(
                "refresh_token", response.getRefreshToken(), "/api/v1/auth", Duration.ofDays(7).toSeconds()
        );

        response.setRefreshToken(null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.of(HttpStatus.OK.value(), message, response));
    }

    private ResponseCookie buildCookie(String name, String value, String path, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) // true en prod HTTPS
                .sameSite("Lax")
                .path(path)
                .maxAge(maxAgeSeconds)
                .build();
    }
}
