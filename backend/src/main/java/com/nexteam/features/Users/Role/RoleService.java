package com.nexteam.features.Users.Role;

import com.nexteam.exceptions.AlreadyExistException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Users.Role.dtos.RoleRequestDTO;
import com.nexteam.features.Users.Role.dtos.RoleResponseDTO;
import com.nexteam.features.Users.Role.dtos.mapper.RoleMapper;
import com.nexteam.features.Users.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    /**
     * Méthode en charge de retourner tous les rôles
     *
     * @param pageable paramètres de pagination et de tri
     * @return tous les rôles de manière paginée
     *
     */
    public Page<RoleResponseDTO> getRoles(Pageable pageable) {
        Page<Role> role = roleRepository.findAll(pageable);
        return role.map(roleMapper::roleToResponseDTO);
    }

    /**
     * Méthode en charge de créer un rôle avec un préfixe 'ROLE_' sauf s'il existe déjà.
     *
     * @param roleRequestDTO les éléments pour créer le rôle
     * @return le rôle crée
     * @throws AlreadyExistException 'Le rôle existe déjà.'
     */
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        roleRepository.findByNameIgnoreCase(roleRequestDTO.getName())
                .ifPresent(role -> {
                    throw new AlreadyExistException("Le rôle existe déjà.");
                });

        Role role = roleMapper.requestDTOToRole(roleRequestDTO);
        role.setName("ROLE_" + role.getName().toUpperCase());

        return roleMapper.roleToResponseDTO(roleRepository.save(role));
    }

    /**
     * Méthode de charge de supprimer un role par son UUID s'il existe et s'il est attribué à l'utilisateur.
     *
     * @param publicId UUID du rôle
     * @throws NotFoundException 'Role non trouvé.'
     */
    @Transactional
    public void deleteRole(UUID publicId) {
        Role role = roleRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundException("Role non trouvé."));

        userRepository.findByRoles_NameIgnoreCase(role.getName(), Pageable.unpaged()).map(user -> {
            user.getRoles().remove(role);
            return userRepository.save(user);
        });

        roleRepository.deleteByPublicId(publicId);
    }

    /**
     * Méthode en charge de retrouver un rôle par son nom s'il existe.
     * @param name le rôle qui nous permet de faire la recherche
     * @return le rôle
     * @throws NotFoundException 'Role non trouvé.'
     */
    public Role getRoleByName(String name){
      return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Role non trouvé."));
    }
}
