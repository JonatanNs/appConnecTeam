package com.nexteam.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private final String secretKey;
    @Getter
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long expirationMs) {
        this.secretKey = secretKey;
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserDetails userDetail) {

        // Récupère toutes les autorités/roles de l'utilisateur et les transforme en une seule chaîne séparée par des virgules.
        String authorities = userDetail.getAuthorities().stream()// renvoie une collection de GrantedAuthority (ex : ROLE_USER, ROLE_ADMIN)
                .map(GrantedAuthority::getAuthority)// extrait le nom de chaque rôle
                .collect(Collectors.joining(",")); //concatène tous les rôles en une seule String, séparés par des virgules

        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userDetail.getUsername())
                .claim("scp", authorities) // role
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs))) // EXPIRATION_KEY en ms
                .signWith(getSecretKey())
                .compact();
    }

    private Key getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Vérifie si un token est valide pour un utilisateur donné
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals((userDetails).getUsername()) && !isTokenExpired(token);
    }

    // Extrait le username (subject) depuis le token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Méthode générique pour extraire n'importe quel claim du token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    // Analyse le token et retourne tous les claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser() // Initialise le parser JJWT
                .verifyWith((SecretKey) getSecretKey()) // Vérifie la signature avec la clé secrète
                .build() // Construit le parser
                .parseSignedClaims(token) // Analyse le token signé
                .getPayload(); // Récupère le contenu (claims)
    }

    // Vérifie si le token est expiré
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrait la date d'expiration depuis le token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
