package com.nexteam.features.Users.User.dtos.mapper;

import com.nexteam.features.Users.User.User;
import com.nexteam.features.Users.User.dtos.UserRequestDTO;
import com.nexteam.features.Users.User.dtos.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Convertit une entité User en DTO de réponse.
     *
     * @param user entité utilisateur
     * @return données utilisateur exposées par l'API
     */
    UserResponseDTO userToResponseDTO(User user);

    /**
     * Convertit un DTO de requête en entité User.
     *
     * @param userRequestDTO données reçues depuis le frontend
     * @return entité User prête à être persistée
     */
    User requestDTOToUser(UserRequestDTO userRequestDTO);
}


