package com.nexteam.features.Messaging.notification.dtos.mapper;

import com.nexteam.features.Messaging.notification.Notification;
import com.nexteam.features.Messaging.notification.dtos.NotifRequestDTO;
import com.nexteam.features.Messaging.notification.dtos.NotificationResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponseDTO notificationToResponseDTO(Notification notification);

    Notification requestDTOToNotification(NotifRequestDTO notifRequestDTO);
}
