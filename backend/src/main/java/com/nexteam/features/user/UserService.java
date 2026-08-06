package com.nexteam.features.user;

import com.nexteam.exception.AlreadyExistException;
import com.nexteam.exception.NotFoundException;
import com.nexteam.features.user.dtos.UserRequestDTO;
import com.nexteam.features.user.dtos.UserResponseDTO;
import com.nexteam.features.user.dtos.mapper.UserMapper;
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
    private final UserMapper userMapper;

    /**
     *
     * Méthode en charge de récupérer tous les utilisateurs avec pagination.
     *
     * @param pageable
     * @return une page d'utilisateurs
     */

    public Page<UserResponseDTO> getUsers(Pageable pageable) {
        Page<User> users = repository.findAll(pageable);
        return users.map(userMapper::userToResponseDTO);
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     *
     * @param publicId
     * @return l'utilisateur trouvé
     */

    public UserResponseDTO getUser(UUID publicId) {

        User user = repository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));

        return userMapper.userToResponseDTO(user);
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email
     * @return l'utilisateur trouvé
     */

    public UserResponseDTO getUserByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));

        return userMapper.userToResponseDTO(user);
    }

    /**
     * Met à jour un utilisateur existant.
     * Vérifie si l'utilisateur existe avant de procéder à la mise à jour.
     *
     * @param publicId
     * @param userRequestDTO
     * @return l'utilisateur mis à jour
     */
    public UserResponseDTO updateUser(UUID publicId, UserRequestDTO userRequestDTO) {

        User existingUser = repository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));

        repository.findByEmail(userRequestDTO.getEmail())
                .filter(user -> !user.getPublicId().equals(publicId))
                .ifPresent(user -> {
                    throw new AlreadyExistException(
                            "L'email est déjà associé à un compte."
                    );
                });

        existingUser.setFirstname(userRequestDTO.getFirstname());
        existingUser.setLastname(userRequestDTO.getLastname());
        existingUser.setEmail(userRequestDTO.getEmail());

        return userMapper.userToResponseDTO(repository.save(existingUser));
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
     * @param userRequestDTO
     * @return l'utilisateur créé
     */
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        repository.findByEmail(userRequestDTO.getEmail()).ifPresent(existingUser -> {
            throw new AlreadyExistException("L'email est déjà associé à un compte.");
        });

        userRequestDTO.setEmail(
                userRequestDTO.getFirstname()
                        .toLowerCase() + "." + userRequestDTO.getLastname().toLowerCase() + "@nexteam.com");

        User user = userMapper.requestDTOToUser(userRequestDTO);

        String rawPassword = generateRandomPassword();
        // TODO: hasher avec BCrypt une fois Spring Security en place
        user.setPassword(rawPassword);

        return userMapper.userToResponseDTO(repository.save(user));
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
