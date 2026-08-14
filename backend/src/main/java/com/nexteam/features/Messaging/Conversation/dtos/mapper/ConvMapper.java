package com.nexteam.features.Messaging.Conversation.dtos.mapper;

import com.nexteam.features.Messaging.Conversation.Conversation;
import com.nexteam.features.Messaging.Conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Messaging.Conversation.dtos.ConvResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConvMapper {
    ConvResponseDTO convToResponseDTO(Conversation conversation);
    Conversation requestDTOToConv(ConvRequestDTO convRequestDTO);
}
