package com.nexteam.security.auth;

import com.nexteam.exceptions.InvalidCredentialsException;
import com.nexteam.features.Users.Role.dtos.RoleResponseDTO;
import com.nexteam.features.Users.Role.dtos.mapper.RoleMapper;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import com.nexteam.security.UserPrincipalService;
import com.nexteam.security.dto.LoginRequestDTO;
import com.nexteam.security.dto.LoginResponseDTO;
import com.nexteam.security.jwt.JwtService;
import com.nexteam.security.refreshToken.Token;
import com.nexteam.security.refreshToken.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserPrincipalService userPrincipalService;
    private final RoleMapper roleMapper;


    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new InvalidCredentialsException("Utilisateur introuvable."));
            String token = jwtService.generateToken(userDetails);
            Token refreshToken = tokenService.createToken(user);

            Set<RoleResponseDTO> roles = user.getRoles().stream()
                    .map(roleMapper::roleToResponseDTO)
                    .collect(Collectors.toSet());

            return LoginResponseDTO.builder()
                    .token(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .online(true)
                    .tokenExpiresIn(jwtService.getExpirationMs())
                    .publicId(user.getPublicId())
                    .email(user.getEmail())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .roles(roles)
                    .build();

        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect.");
        }
    }

    public LoginResponseDTO refresh(String refreshTokenValue) {
        Token oldToken = tokenService.validateToken(refreshTokenValue);
        User user = oldToken.getUser();

        Token newToken = tokenService.rotateToken(oldToken);

        UserDetails userDetails = userPrincipalService.loadUserByUsername(user.getEmail());
        String newAccessToken = jwtService.generateToken(userDetails);

        Set<RoleResponseDTO> roles = user.getRoles().stream()
                .map(roleMapper::roleToResponseDTO)
                .collect(Collectors.toSet());

        return LoginResponseDTO.builder()
                .token(newAccessToken)
                .refreshToken(newToken.getRefreshToken())
                .tokenExpiresIn(jwtService.getExpirationMs())
                .online(true)
                .publicId(user.getPublicId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .roles(roles)
                .build();
    }
}
