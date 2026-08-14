package com.nexteam.features.Users.Role.dtos.mapper;

import com.nexteam.features.Users.Role.Role;
import com.nexteam.features.Users.Role.dtos.RoleRequestDTO;
import com.nexteam.features.Users.Role.dtos.RoleResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role requestDTOToRole(RoleRequestDTO requestDTO);
    RoleResponseDTO roleToResponseDTO(Role role);
}
