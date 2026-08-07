package com.nexteam.features.Role.dtos.mapper;

import com.nexteam.features.Role.Role;
import com.nexteam.features.Role.dtos.RoleRequestDTO;
import com.nexteam.features.Role.dtos.RoleResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role requestDTOToRole(RoleRequestDTO requestDTO);
    RoleResponseDTO roleToResponseDTO(Role role);
}
