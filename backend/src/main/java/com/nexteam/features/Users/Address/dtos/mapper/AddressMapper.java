package com.nexteam.features.Users.Address.dtos.mapper;

import com.nexteam.features.Users.Address.Address;
import com.nexteam.features.Users.Address.dtos.AddressRequestDTO;
import com.nexteam.features.Users.Address.dtos.AddressResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address requestDTOtoAddress(AddressRequestDTO addressRequestDTO);
    AddressResponseDTO addressToResponseDTO(Address address);
}
