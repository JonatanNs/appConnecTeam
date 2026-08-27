package com.nexteam.features.Users.Address;

import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Users.Address.dtos.AddressRequestDTO;
import com.nexteam.features.Users.Address.dtos.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    /**
     * Méthode en charge de trouver une adresse grâce à un UUID si elle existe
     *
     * @param publicId UUID de l'adresse
     * @return l'adresse trouvé
     * @throws NotFoundException 'Address non trouvé.'
     */
    public Address getAddressById(UUID publicId) {
        return addressRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Address non trouvé."));
    }

    /**
     * Méthode en charge de créer l'adresse
     *
     * @param addressRequestDTO les informations nécessaires pour créer l'adresse
     * @return l'adresse crée
     */
    public Address createAddress(AddressRequestDTO addressRequestDTO) {
        Address address = addressMapper.requestDTOtoAddress(addressRequestDTO);
        return addressRepository.save(address);
    }

    /**
     * Méthode en charge de supprimer une adresse par son UUID si elle existe.
     *
     * @param publicId UUID de l'adresse
     * @throws NotFoundException 'Address non trouvé'.
     */
    public void deleteAddress(UUID publicId) {
        Address address = addressRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Address non trouvé."));
        addressRepository.deleteByPublicId(publicId);
    }
}
