package com.nexteam.features.Messaging.notification.dtos.mapper;

import com.nexteam.features.Messaging.notification.Notification;
import com.nexteam.features.Messaging.notification.dtos.NotifRequestDTO;
import com.nexteam.features.Messaging.notification.dtos.NotificationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "conversationPublicId", source = "conversation.publicId")
    @Mapping(target = "messagePublicId", source = "message.publicId")
    NotificationResponseDTO notificationToResponseDTO(Notification notification);

    Notification requestDTOToNotification(NotifRequestDTO notifRequestDTO);
}