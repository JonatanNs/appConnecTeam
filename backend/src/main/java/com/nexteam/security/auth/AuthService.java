package com.nexteam.security.auth;

import com.nexteam.exceptions.InvalidCredentialsException;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import com.nexteam.security.dto.LoginRequestDTO;
import com.nexteam.security.dto.LoginResponseDTO;
import com.nexteam.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new InvalidCredentialsException("Utilisateur introuvable."));
            String token = jwtService.generateToken(userDetails);

            return LoginResponseDTO.builder()
                    .token(token)
                    .publicId(user.getPublicId())
                    .email(user.getEmail())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .build();

        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect.");
        }
    }
}
