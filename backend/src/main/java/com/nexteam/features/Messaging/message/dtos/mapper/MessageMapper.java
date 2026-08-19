package com.nexteam.features.Messaging.message.dtos.mapper;

import com.nexteam.features.Messaging.message.Message;
import com.nexteam.features.Messaging.message.dtos.MessageRequestDTO;
import com.nexteam.features.Messaging.message.dtos.MessageResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender.publicId", target = "senderPublicId")
    @Mapping(target = "senderName", expression = "java(message.getSender().getFirstname() + \" \" + message.getSender().getLastname())")
    MessageResponseDTO messageToResponseDTO(Message message);

    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "type", ignore = true)
    Message requestDTOToMessage(MessageRequestDTO messageRequestDTO);
}
