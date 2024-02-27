package com.martikan.employeeapi.common;

import org.mapstruct.MappingTarget;

public interface BaseMapper<E, D> {
    E toEntity(D source);
    D toDTO (E source);
    E updateEntity(D source, @MappingTarget E target);
}
