package com.nexteam.features.Address.dtos.mapper;

import com.nexteam.features.Address.Address;
import com.nexteam.features.Address.dtos.AddressRequestDTO;
import com.nexteam.features.Address.dtos.AddressResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address requestDTOtoAddress(AddressRequestDTO addressRequestDTO);
    AddressResponseDTO addressToResponseDTO(Address address);
}
