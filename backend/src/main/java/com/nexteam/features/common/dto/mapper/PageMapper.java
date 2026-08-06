package com.nexteam.features.common.dto.mapper;

import com.nexteam.features.common.dto.PageResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageMapper {

    /**
     * Convertit une Page Spring Data en PageResponseDTO.
     *
     * @param page page contenant les éléments
     * @return réponse paginée
     */
    default <T> PageResponseDTO<T> toPageResponse(Page<T> page) {

        return PageResponseDTO.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}
