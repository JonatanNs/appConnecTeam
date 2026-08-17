package com.nexteam.features.notification.dtos.mapper;

import com.nexteam.features.notification.Notification;
import com.nexteam.features.notification.dtos.NotifRequestDTO;
import com.nexteam.features.notification.dtos.NotificationResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponseDTO notificationToResponseDTO(Notification notification);

    Notification requestDTOToNotification(NotifRequestDTO notifRequestDTO);
}
