package com.nexteam.features.user;

import com.nexteam.exception.AlreadyExistException;
import com.nexteam.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Classe 'UserService' en charge de la logique métier pour la gestion des utilisateurs.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789!@#$%";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     *
     * Méthode en charge de récupérer tous les utilisateurs avec pagination.
     *
     * @param pageable
     * @return une page d'utilisateurs
     */

    public Page<User> getUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     *
     * @param publicId
     * @return l'utilisateur trouvé
     */

    public User getUser(UUID publicId) {
        return repository.findByPublicId(publicId).orElseThrow(() -> new NotFoundException("Élément non trouvé."));
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email
     * @return l'utilisateur trouvé
     */

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Élément non trouvé."));
    }

    /**
     * Met à jour un utilisateur existant.
     * Vérifie si l'utilisateur existe avant de procéder à la mise à jour.
     *
     * @param publicId
     * @param user
     * @return l'utilisateur mis à jour
     */

    public User updateUser(UUID publicId, User user) {
        User existingUser = repository.findByPublicId(publicId).orElseThrow(() -> new NotFoundException("Élément non trouvé."));

        repository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new AlreadyExistException("L'email est déjà associé à un compte.");
        });

        user.setId(existingUser.getId());
        user.setVersion(existingUser.getVersion());
        return repository.save(user);
    }

    private String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * Crée un nouvel utilisateur.
     * Vérifie si l'email est déjà associé à un compte existant.
     *
     * @param user
     * @return l'utilisateur créé
     */
    public User createUser(User user) {
        repository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new AlreadyExistException("L'email est déjà associé à un compte.");
        });

        String rawPassword = generateRandomPassword();
        // TODO: hasher avec BCrypt une fois Spring Security en place
        user.setPassword(rawPassword);
        user.setEmail(user.getFirstname().toLowerCase() + "." + user.getLastname().toLowerCase() + "@nexteam.com");

        return repository.save(user);
    }


    /**
     * Supprime un utilisateur par son identifiant unique.
     *
     * @param publicId
     */
    @Transactional

    public void deleteUser(UUID publicId) {
        repository.findByPublicId(publicId).orElseThrow(() -> new NotFoundException("Élément non trouvé."));
        repository.deleteByPublicId(publicId);
    }

    @Transactional
    public void activateUser(UUID publicId) {
        User user = repository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));
        user.setActive(true);
    }

    @Transactional
    public void deactivateUser(UUID publicId) {
        User user = repository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));
        user.setActive(false);
    }
}
