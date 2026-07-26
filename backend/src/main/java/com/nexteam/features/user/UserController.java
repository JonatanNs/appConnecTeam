package com.nexteam.features.user;

import com.nexteam.features.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("api/v1")
@RestController
public class UserController {
    private final UserService userService;

    /**
     * Récupère tous les utilisateurs avec pagination.
     *
     * @param pageable les paramètres de pagination
     * @return une réponse contenant une page d'utilisateurs
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(Pageable pageable) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Éléments récupérés avec succes.",
                        userService.getUsers(pageable)
                )
        );
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     *
     * @param id l'UUID de l'utilisateur
     * @return une réponse contenant l'utilisateur demandé
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable UUID id) {
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
    @GetMapping("/user/email")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@RequestParam String email) {
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
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
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
     * @param user l'objet utilisateur avec les données mises à jour (validé)
     * @return une réponse contenant l'utilisateur modifié
     */
    @PutMapping("/user/{publicId}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID publicId, @Valid @RequestBody User user) {
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
    @DeleteMapping("/user/supprimer/{publicId}")
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

    @PutMapping("/user/desactiver/{publicId}")
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

    @PutMapping("/user/active/{publicId}")
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
}

