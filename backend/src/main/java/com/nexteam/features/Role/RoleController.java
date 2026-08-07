package com.nexteam.features.Role;

import com.nexteam.features.Role.dtos.RoleRequestDTO;
import com.nexteam.features.Role.dtos.RoleResponseDTO;
import com.nexteam.features.common.ApiResponse;
import com.nexteam.features.common.dto.PageResponseDTO;
import com.nexteam.features.common.dto.mapper.PageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final PageMapper pageMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<RoleResponseDTO>>> getRoles(Pageable pageable) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Roles récupérés avec succes.",
                       pageMapper.toPageResponse(roleService.getRoles(pageable))
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRoles(
            @Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Role " + roleRequestDTO.getName() + " créé avec succes.",
                        roleService.createRole(roleRequestDTO)
                )
        );
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> deleteRoles(@PathVariable UUID publicId) {
        roleService.deleteRole(publicId);
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Role supprimé avec succès.",
                        null
                )
        );
    }
}
