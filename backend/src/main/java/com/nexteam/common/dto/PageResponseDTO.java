package com.nexteam.common.dto;

import lombok.*;

import java.util.List;

/**
 * Classe 'PageResponseDTO' en charge d'exposer les informations nécessaires au client sans
 *  exposer directement tous les éléments de l'instance Page.
 *
 * @author jnsualu2026
 * @since 2026-07-27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;

}