package com.nexteam.features.address.dtos.mapper;

import com.nexteam.features.address.Address;
import com.nexteam.features.address.dtos.AddressRequestDTO;
import com.nexteam.features.address.dtos.AddressResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address requestDTOtoAddress(AddressRequestDTO addressRequestDTO);
    AddressResponseDTO addressToResponseDTO(Address address);
}
