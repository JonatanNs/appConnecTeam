package com.nexteam.features.Conversation.dtos.mapper;

import com.nexteam.features.Conversation.Conversation;
import com.nexteam.features.Conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Conversation.dtos.ConvResponseDTO;
import com.nexteam.websocket.messaging.message.dtos.mapper.MessageMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface ConvMapper {
    ConvResponseDTO convToResponseDTO(Conversation conversation);
    Conversation requestDTOToConv(ConvRequestDTO convRequestDTO);
}
