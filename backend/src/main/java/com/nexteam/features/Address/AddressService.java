package com.nexteam.features.Address;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Address.dtos.AddressRequestDTO;
import com.nexteam.features.Address.dtos.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
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

    public void deleteAddress(UUID publicId) {
        Address address = addressRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Address non trouvé."));
        addressRepository.deleteByPublicId(publicId);
    }
}
