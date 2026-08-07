package com.nexteam.features.User;

import com.nexteam.exceptions.AlreadyExistException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Address.Address;
import com.nexteam.features.Address.AddressService;
import com.nexteam.features.Address.dtos.AddressRequestDTO;
import com.nexteam.features.Role.Role;
import com.nexteam.features.Role.RoleService;
import com.nexteam.features.Role.dtos.RoleRequestDTO;
import com.nexteam.features.Role.dtos.mapper.RoleMapper;
import com.nexteam.features.User.dtos.UserRequestDTO;
import com.nexteam.features.User.dtos.UserResponseDTO;
import com.nexteam.features.User.dtos.mapper.UserMapper;
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

    private final UserRepository userRepository;

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789!@#$%";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UserMapper userMapper;
    private final AddressService addressService;
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    /**
     *
     * Méthode en charge de récupérer tous les utilisateurs avec pagination.
     *
     * @param pageable
     * @return une page d'utilisateurs
     */

    public Page<UserResponseDTO> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::userToResponseDTO);
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     *
     * @param publicId
     * @return l'utilisateur trouvé
     */

    public UserResponseDTO getUser(UUID publicId) {

        User user = userRepository.findByPublicId(publicId)
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
        User user = userRepository.findByEmail(email)
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

        User existingUser = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));

        userRepository.findByEmail(userRequestDTO.getEmail())
                .filter(user -> !user.getPublicId().equals(publicId))
                .ifPresent(user -> {
                    throw new AlreadyExistException(
                            "L'email est déjà associé à un compte."
                    );
                });

        existingUser.setFirstname(userRequestDTO.getFirstname());
        existingUser.setLastname(userRequestDTO.getLastname());
        existingUser.setEmail(userRequestDTO.getEmail());

        return userMapper.userToResponseDTO(userRepository.save(existingUser));
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

        String email = userRequestDTO.getFirstname().toLowerCase()
                + "."
                + userRequestDTO.getLastname().toLowerCase()
                + "@nexteam.com";

        userRepository.findByEmail(email).ifPresent(existingUser -> {
            throw new AlreadyExistException("L'email est déjà associé à un compte.");
        });

        userRequestDTO.setEmail(email);

        User user = userMapper.requestDTOToUser(userRequestDTO);

        user.setActive(true);

        String rawPassword = generateRandomPassword();
        user.setPassword(rawPassword);
        //user.setPassword(passwordEncoder.encode(rawPassword));

        return userMapper.userToResponseDTO(userRepository.save(user));
    }

    /**
     * Supprime un utilisateur par son identifiant unique.
     *
     * @param publicId
     */
    @Transactional

    public void deleteUser(UUID publicId) {
        userRepository.findByPublicId(publicId).orElseThrow(() -> new NotFoundException("Élément non trouvé."));
        userRepository.deleteByPublicId(publicId);
    }

    @Transactional
    public void activateUser(UUID publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));
        user.setActive(true);
    }

    @Transactional
    public void deactivateUser(UUID publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Élément non trouvé."));
        user.setActive(false);
    }

    public Page<UserResponseDTO> searchUsers(String firstname, String lastname, Pageable pageable) {
        Page<User> users = userRepository.search(firstname, lastname, pageable);
        return users.map(userMapper::userToResponseDTO);
    }

    public UserResponseDTO addAddress(UUID userPublicId, AddressRequestDTO addressRequestDTO) {

        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        Address address = addressService.createAddress(addressRequestDTO);

        user.setAddress(address);

        return userMapper.userToResponseDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteAddress(UUID userPublicId) {

        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        Address address = user.getAddress();

        if (address != null) {
            user.setAddress(null);
            addressService.deleteAddress(address.getPublicId());
            userRepository.save(user);
        } else{
            throw new NotFoundException("Aucune address répertorié.");
        }
    }

    public Page<UserResponseDTO> getUsersByRoles(String nameRole, Pageable pageable){
        roleService.getRoleByName(nameRole);

        return userRepository.findByRoles_NameIgnoreCase(nameRole, pageable)
                .map(userMapper::userToResponseDTO);
    }

    @Transactional
    public UserResponseDTO addRole(UUID userPublicId, RoleRequestDTO roleRequestDTO) {
        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        Role role = roleService.getRoleByName(roleRequestDTO.getName());
        user.getRoles().add(role);

        return userMapper.userToResponseDTO(userRepository.save(user));
    }

}
