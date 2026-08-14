package com.nexteam.features.Messaging.Message.dtos.mapper;

import com.nexteam.features.Messaging.Message.Message;
import com.nexteam.features.Messaging.Message.dtos.MessageRequestDTO;
import com.nexteam.features.Messaging.Message.dtos.MessageResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponseDTO messageToResponseDTO(Message message);
    Message requestDTOToMessage(MessageRequestDTO messageRequestDTO);
}
