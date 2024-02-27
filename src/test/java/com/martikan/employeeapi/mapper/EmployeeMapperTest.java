package com.martikan.employeeapi.mapper;

import com.martikan.employeeapi.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext
@SpringBootTest(classes = {EmployeeMapper.class, EmployeeMapperImpl.class})
class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void testToDTO_happyFlow() {
        final var employee = Faker.createRandomEmployee();

        final var actualDTO = employeeMapper.toDTO(employee);

        assertNotNull(actualDTO);
        assertEquals(employee.getId(), actualDTO.getId());
        assertEquals(employee.getEmail(), actualDTO.getEmail());
        assertEquals(employee.getFirstName(), actualDTO.getFirstName());
        assertEquals(employee.getLastName(), actualDTO.getLastName());
    }

    @Test
    void testToEntity_happyFlow() {
        final var employeeDTO = Faker.createRandomEmployeeDTO();

        final var actualEntity = employeeMapper.toEntity(employeeDTO);

        assertNotNull(actualEntity);
        assertEquals(employeeDTO.getId(), actualEntity.getId());
        assertEquals(employeeDTO.getEmail(), actualEntity.getEmail());
        assertEquals(employeeDTO.getFirstName(), actualEntity.getFirstName());
        assertEquals(employeeDTO.getLastName(), actualEntity.getLastName());
    }

    @Test
    void testToUpdateEntity_happyFlow() {
        final var employeeDTO = Faker.createRandomEmployeeDTO();
        final var employee = Faker.createRandomEmployee();

        final var updatedEntity = employeeMapper.updateEntity(employeeDTO, employee);

        assertNotNull(updatedEntity);
        assertEquals(employeeDTO.getId(), updatedEntity.getId());
        assertEquals(employeeDTO.getEmail(), updatedEntity.getEmail());
        assertEquals(employeeDTO.getFirstName(), updatedEntity.getFirstName());
        assertEquals(employeeDTO.getLastName(), updatedEntity.getLastName());
    }

}
