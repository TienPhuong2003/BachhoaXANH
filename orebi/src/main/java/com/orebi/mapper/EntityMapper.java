package com.orebi.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface EntityMapper<D, E> {
    
    D toDTO(E entity);
    E toEntity(D dto);
    
    default List<D> toDTOList(List<E> entityList) {
        if (entityList == null) {
            return Collections.emptyList(); 
        }
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    default List<E> toEntityList(List<D> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
