package com.nexteam.features.Conversation;

import com.nexteam.common.ApiResponse;
import com.nexteam.common.dto.PageResponseDTO;
import com.nexteam.common.dto.mapper.PageMapper;
import com.nexteam.features.Conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Conversation.dtos.ConvResponseDTO;
import com.nexteam.features.Users.User.UserService;
import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Class 'ConversationController' en charge de la gestion des conversations.
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 22:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/conversations")
public class ConversationController {
    private final ConversationService conversationService;
    private final UserService userService;
    private final PageMapper pageMapper;

    @GetMapping("/{publicId}")
    public ResponseEntity<ApiResponse<ConvResponseDTO>> getConvByPublicId(
            @PathVariable UUID publicId, @AuthenticationPrincipal UserDetails principal) {
        conversationService.assertParticipant(publicId, principal.getUsername());
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversation trouvé avec succès.",
                        conversationService.getByPublicId(publicId))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ConvResponseDTO>>> getConvByNameContaining(
            @RequestParam("word") String word,
            Pageable pageable) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Conversations trouvé avec succès.",
                        conversationService.getByNameContaining(word, pageable)
                )
        );
    }

    @GetMapping("/users/{userPublicId}")
    public ResponseEntity<ApiResponse<PageResponseDTO<ConvResponseDTO>>> getConvByUserUUID(
            @PathVariable UUID userPublicId,
            Pageable pageable,
            @AuthenticationPrincipal UserDetails principal) {

        UserResponseDTO requester = userService.getUserByEmail(principal.getUsername());
        if (!requester.getPublicId().equals(userPublicId)) {
            throw new AccessDeniedException("Vous ne pouvez consulter que vos propres conversations.");
        }

        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversations trouvé avec succès.",
                        pageMapper.toPageResponse(conversationService.getByUsersPublicId(userPublicId, pageable)))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConvResponseDTO>> createConversation(
            @Valid @RequestBody ConvRequestDTO requestDTO, @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversation créée avec succès.",
                        conversationService.createConversation(requestDTO, principal.getUsername()))
        );
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<ApiResponse<ConvResponseDTO>> updateConversation(
            @PathVariable UUID publicId,
            @Valid @RequestBody ConvRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails principal) {
        conversationService.assertOwner(publicId, principal.getUsername());
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversation modifiée avec succès.",
                        conversationService.updateConversation(publicId, requestDTO))
        );
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<ApiResponse<Void>> deleteConversation(
            @PathVariable UUID publicId, @AuthenticationPrincipal UserDetails principal) {
        conversationService.assertOwner(publicId, principal.getUsername());
        conversationService.deleteConversation(publicId);
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversation supprimée avec succès.", null)
        );
    }
}