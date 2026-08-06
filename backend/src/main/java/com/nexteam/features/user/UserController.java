package com.nexteam.features.user;

import com.nexteam.features.address.AddressService;
import com.nexteam.features.address.dtos.AddressRequestDTO;
import com.nexteam.features.common.ApiResponse;
import com.nexteam.features.common.dto.PageResponseDTO;
import com.nexteam.features.common.dto.mapper.PageMapper;
import com.nexteam.features.user.dtos.UserRequestDTO;
import com.nexteam.features.user.dtos.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Classe 'UserController' en charge de gérer les opérations CRUD sur les utilisateurs.
 * Fournit les endpoints REST pour créer, lire, mettre à jour et supprimer des utilisateurs.
 *
 * @author jnsualu2026
 * @since 2026-06-19
 */
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@RestController
public class UserController {
    private final UserService userService;
    private final AddressService addressService;
    private final PageMapper pageMapper;

    /**
     * Récupère tous les utilisateurs avec pagination.
     *
     * @param pageable les paramètres de pagination
     * @return une réponse contenant une page d'utilisateurs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<UserResponseDTO>>> getUsers(Pageable pageable) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Éléments récupérés avec succes.",
                        pageMapper.toPageResponse(userService.getUsers(pageable))
                )
        );
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     *
     * @param id l'UUID de l'utilisateur
     * @return une réponse contenant l'utilisateur demandé
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Élément récupéré avec succes.",
                        userService.getUser(id)
                )
        );
    }

    /**
     * Récupère un utilisateur par son adresse email.
     *
     * @param email l'adresse email de l'utilisateur
     * @return une réponse contenant l'utilisateur recherché
     */
    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "OK",
                        userService.getUserByEmail(email))
        );
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param user l'objet utilisateur à créer (validé)
     * @return une réponse contenant l'utilisateur créé
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserRequestDTO user) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Utilisateur crée avec succes.",
                        userService.createUser(user)
                )
        );
    }

    /**
     * Met à jour un utilisateur existant.
     *
     * @param publicId l'UUID de l'utilisateur à mettre à jour
     * @param user     l'objet utilisateur avec les données mises à jour (validé)
     * @return une réponse contenant l'utilisateur modifié
     */
    @PutMapping("/{publicId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID publicId,
            @Valid @RequestBody UserRequestDTO user) {

        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Utilisateur modifié avec succes.",
                        userService.updateUser(publicId, user)
                )
        );
    }

    /**
     * Supprime un utilisateur.
     *
     * @param publicId l'UUID de l'utilisateur à supprimer
     * @return une réponse de confirmation de suppression
     */
    @DeleteMapping("/{publicId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID publicId) {
        userService.deleteUser(publicId);
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Utilisateur supprimé avec succes.",
                        null
                )
        );
    }

    @PutMapping("/deactivate/{publicId}")
    public ResponseEntity<ApiResponse<Void>> desactivateUser(@PathVariable UUID publicId) {
        userService.deactivateUser(publicId);
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Utilisateur désactivé avec succes.",
                        null
                )
        );
    }

    @PutMapping("/activate/{publicId}")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable UUID publicId) {
        userService.activateUser(publicId);
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Utilisateur activé avec succes.",
                        null
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponseDTO<UserResponseDTO>>> searchUsers(
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            Pageable pageable
    ) {
        PageResponseDTO<UserResponseDTO> users = pageMapper.toPageResponse(userService.searchUsers(firstname, lastname, pageable));

        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Utilisateurs trouvés.",
                        users
                )
        );
    }

    @DeleteMapping("/{userPublicId}/me/addresses")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable UUID userPublicId) {
        userService.deleteAddress(userPublicId);
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Adresse supprimée avec succès.",
                        null
                )
        );
    }

    @PostMapping("/{userPublicId}/me/addresses")
    public ResponseEntity<ApiResponse<UserResponseDTO>> addAddress(@PathVariable UUID userPublicId, @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        UserResponseDTO userResponseDTO = userService.addAddress(userPublicId, addressRequestDTO);
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Adresse ajoutée avec succès.",
                        userResponseDTO
                )
        );
    }
}
