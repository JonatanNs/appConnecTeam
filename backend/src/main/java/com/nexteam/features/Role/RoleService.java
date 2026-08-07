package com.nexteam.features.Role;

import com.nexteam.exceptions.AlreadyExistException;
import com.nexteam.exceptions.NotFoundException;
import com.nexteam.features.Role.dtos.RoleRequestDTO;
import com.nexteam.features.Role.dtos.RoleResponseDTO;
import com.nexteam.features.Role.dtos.mapper.RoleMapper;
import com.nexteam.features.User.UserRepository;
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

    public Page<RoleResponseDTO> getRoles(Pageable pageable) {
        Page<Role> role = roleRepository.findAll(pageable);
        return role.map(roleMapper::roleToResponseDTO);
    }

    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        roleRepository.findByNameIgnoreCase(roleRequestDTO.getName())
                .ifPresent(role -> {
                    throw new AlreadyExistException("Le rôle existe déjà.");
                });

        Role role = roleMapper.requestDTOToRole(roleRequestDTO);

        return roleMapper.roleToResponseDTO(roleRepository.save(role));
    }

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

    public Role getRoleByName(String name){
      return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Role non trouvé."));
    }


}
