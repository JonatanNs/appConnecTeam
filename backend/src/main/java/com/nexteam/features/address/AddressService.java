package com.nexteam.features.address;

import com.nexteam.exception.NotFoundException;
import com.nexteam.features.address.dtos.AddressRequestDTO;
import com.nexteam.features.address.dtos.AddressResponseDTO;
import com.nexteam.features.address.dtos.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Address getAddressById(UUID publicId) {
        return addressRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Address non trouvé."));
    }

    public Address createAddress(AddressRequestDTO addressRequestDTO) {
        Address address = addressMapper.requestDTOtoAddress(addressRequestDTO);
        return addressRepository.save(address);
    }

    public AddressResponseDTO deleteAddress(UUID publicId) {
        Address address = addressRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Address non trouvé."));
        addressRepository.deleteByPublicId(publicId);
        return addressMapper.addressToResponseDTO(address);
    }
}
