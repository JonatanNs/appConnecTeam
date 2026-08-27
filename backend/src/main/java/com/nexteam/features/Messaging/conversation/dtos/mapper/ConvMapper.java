package com.nexteam.features.Messaging.conversation.dtos.mapper;

import com.nexteam.features.Messaging.conversation.Conversation;
import com.nexteam.features.Messaging.conversation.dtos.ConvRequestDTO;
import com.nexteam.features.Messaging.conversation.dtos.ConvResponseDTO;
import com.nexteam.features.Messaging.message.dtos.mapper.MessageMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface ConvMapper {
    /**
     * Convertie une 'Conversation' en responseDTO en charge d'exposer les informations nécessaires au client sans
     * exposer directement l'entité 'Conversation'.
     * @param conversation données à convertir
     * @return seulement les informations nécessaires de 'Conversation'
     */
    ConvResponseDTO convToResponseDTO(Conversation conversation);

    /**
     * Convertit une 'ConvRequestDTO' en entité 'Conversation'.
     *
     * @param convRequestDTO données de la requête à convertir
     * @return l'entité 'Conversation' correspondante
     */
    Conversation requestDTOToConv(ConvRequestDTO convRequestDTO);
}
