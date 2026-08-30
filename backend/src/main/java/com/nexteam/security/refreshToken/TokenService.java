package com.nexteam.security.refreshToken;

import com.nexteam.exceptions.InvalidTokenException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpirationMs;

    /**
     * Crée et persiste un nouveau refresh token pour l'utilisateur donné.
     */
    @Transactional
    public Token createToken(User user) {
        Token token = Token.builder()
                .user(user)
                .refreshToken(UUID.randomUUID().toString())
                .revoked(false)
                .expiresAt(Instant.now().plusMillis(refreshExpirationMs))
                .build();

        return tokenRepository.save(token);
    }

    public Token findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Token invalide."));
    }

    /**
     * Valide un refresh token : doit exister, ne pas être révoqué, et ne pas être expiré.
     *
     * @throws NotFoundException     si le token n'existe pas
     * @throws InvalidTokenException si le token est révoqué ou expiré
     */
    public Token validateToken(String refreshToken) {
        Token token = findByRefreshToken(refreshToken);

        if (Boolean.TRUE.equals(token.getRevoked())) {
            throw new InvalidTokenException("Refresh token révoqué.");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token expiré.");
        }

        return token;
    }

    /**
     * Révoque un refresh token précis (ex: lors du logout).
     */
    @Transactional
    public void revokeToken(String refreshToken) {
        Token token = findByRefreshToken(refreshToken);
        token.setRevoked(true);
        tokenRepository.save(token);
    }

    @Transactional
    public void deleteByUser_Id(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));
        tokenRepository.deleteByUser_Id(userId);
    }

    @Transactional
    public Token rotateToken(Token oldToken) {
        oldToken.setRevoked(true);
        tokenRepository.save(oldToken);

        return createToken(oldToken.getUser());
    }

    @Transactional
    public void revokeAllForUser(User user) {
        tokenRepository.revokeAllByUser(user);
    }
}
