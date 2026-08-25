package com.nexteam.features.Messaging.notification;

import com.nexteam.common.ApiResponse;
import com.nexteam.common.dto.PageResponseDTO;
import com.nexteam.common.dto.mapper.PageMapper;
import com.nexteam.features.Messaging.notification.dtos.NotificationResponseDTO;
import com.nexteam.features.Users.User.UserService;
import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Class 'NotificationController' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 22:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final PageMapper pageMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<NotificationResponseDTO>>> getMyNotifications(
            Pageable pageable,
            @AuthenticationPrincipal UserDetails principal) {
        UserResponseDTO user = userService.getUserByEmail(principal.getUsername());
        UUID recipientPublicId = user.getPublicId();
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Notifications récupérées avec succès.",
                        pageMapper.toPageResponse(notificationService.getNotifications(recipientPublicId, pageable)))
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@AuthenticationPrincipal UserDetails principal) {
        UserResponseDTO user = userService.getUserByEmail(principal.getUsername());
        UUID recipientPublicId = user.getPublicId();
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Compteur récupéré.",
                        notificationService.getUnreadCount(recipientPublicId))
        );
    }

    @PatchMapping("/{publicId}/read")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> markAsRead(
            @PathVariable UUID publicId, @AuthenticationPrincipal UserDetails principal) {
        UserResponseDTO user = userService.getUserByEmail(principal.getUsername());
        UUID recipientPublicId = user.getPublicId();
        return ResponseEntity.ok().body(
                ApiResponse.of(HttpStatus.OK.value(), "Notification marquée comme lue.",
                        notificationService.markAsRead(publicId, recipientPublicId))
        );
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@AuthenticationPrincipal UserDetails principal) {
        UserResponseDTO user = userService.getUserByEmail(principal.getUsername());
        UUID recipientPublicId = user.getPublicId();
        notificationService.markAllAsRead(recipientPublicId);
        return ResponseEntity.ok().body(ApiResponse.of(HttpStatus.OK.value(), "Tout marqué comme lu.", null));
    }

    @PatchMapping("/conversation/{conversationPublicId}/read")
    public ResponseEntity<ApiResponse<Void>> markConversationNotificationsAsRead(
            @PathVariable UUID conversationPublicId,
            @AuthenticationPrincipal UserDetails principal) {
        UserResponseDTO user = userService.getUserByEmail(principal.getUsername());
        notificationService.markConversationAsRead(conversationPublicId, user.getPublicId());
        return ResponseEntity.ok().body(ApiResponse.of(HttpStatus.OK.value(), "Notifications de la conversation marquées comme lues.", null));
    }
}
