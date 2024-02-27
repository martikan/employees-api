package com.martikan.employeeapi.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class EmployeeDTOTest {

    @Test
    void testEquals_whenSameObjectGiven_returnTrue() {
        final var dto = EmployeeDTO.builder()
            .id(UUID.randomUUID())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();
        final var sameDto = EmployeeDTO.builder()
            .id(dto.getId())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();

        final var res = dto.equals(sameDto);

        assertTrue(res);
    }

    @Test
    void testEquals_whenDifferentObjectGiven_returnFalse() {
        final var dto = EmployeeDTO.builder()
            .id(UUID.randomUUID())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();
        final var differentDto = EmployeeDTO.builder()
            .id(UUID.randomUUID())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();

        final var res = dto.equals(differentDto);

        assertFalse(res);
    }

    @Test
    void testHashCode_whenSameObjectGiven_returnTrue() {
        final var dto = EmployeeDTO.builder()
            .id(UUID.randomUUID())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();
        final var sameDto = EmployeeDTO.builder()
            .id(dto.getId())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();

        final var dtoHash = dto.hashCode();
        final var sameDtoHash = sameDto.hashCode();

        assertEquals(dtoHash, sameDtoHash);
    }

    @Test
    void testHashCode_whenDifferentObjectGiven_returnFalse() {
        final var dto = EmployeeDTO.builder()
            .id(UUID.randomUUID())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();
        final var sameDto = EmployeeDTO.builder()
            .id(UUID.randomUUID())
            .lastName("test")
            .firstName("test")
            .email("test@gmail.com")
            .build();

        final var dtoHash = dto.hashCode();
        final var differentDtoHash = sameDto.hashCode();

        assertNotEquals(dtoHash, differentDtoHash);
    }

}
