package com.nexteam.features.Messaging.conversation.dtos.mapper;

import com.nexteam.features.Messaging.conversation.Conversation;
import com.nexteam.features.Messaging.conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Messaging.conversation.dtos.ConvResponseDTO;
import com.nexteam.features.Messaging.message.dtos.mapper.MessageMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface ConvMapper {
    ConvResponseDTO convToResponseDTO(Conversation conversation);
    Conversation requestDTOToConv(ConvRequestDTO convRequestDTO);
}
