package com.nexteam.features.Messaging.Conversation;

import com.nexteam.common.ApiResponse;
import com.nexteam.common.dto.PageResponseDTO;
import com.nexteam.common.dto.mapper.PageMapper;
import com.nexteam.features.Messaging.Conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Messaging.Conversation.dtos.ConvResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Class 'ConversationController' en charge de
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
    private final PageMapper pageMapper;

    @GetMapping("/{publicId}")
    public ResponseEntity<ApiResponse<ConvResponseDTO>> getConvByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Conversation trouvé avec succès.",
                        conversationService.getByPublicId(publicId)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ConvResponseDTO>>> getConvByNameContaining(@RequestParam("word") String word) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Conversations trouvé avec succès.",
                        conversationService.getByNameContaining(word)
                )
        );
    }

    @GetMapping("/users/{userPublicId}")
    public ResponseEntity<ApiResponse<PageResponseDTO<ConvResponseDTO>>> getConvByUserUUID(@PathVariable UUID userPublicId, Pageable pageable) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Conversations trouvé avec succès.",
                        pageMapper.toPageResponse(conversationService.getByUsersPublicId(userPublicId, pageable))
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConvResponseDTO>> createConversation(@Valid @RequestBody ConvRequestDTO requestDTO) {
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversation créée avec succès.",
                        conversationService.createConversation(requestDTO))
        );
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<ApiResponse<ConvResponseDTO>> updateConversation(
            @PathVariable UUID publicId, @Valid @RequestBody ConvRequestDTO requestDTO) {
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversation modifiée avec succès.",
                        conversationService.updateConversation(publicId, requestDTO))
        );
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<ApiResponse<Void>> deleteConversation(@PathVariable UUID publicId) {
        conversationService.deleteConversation(publicId);
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Conversation supprimée avec succès.", null)
        );
    }
}
