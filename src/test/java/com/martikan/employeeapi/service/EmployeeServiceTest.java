package com.martikan.employeeapi.service;

import com.martikan.employeeapi.Faker;
import com.martikan.employeeapi.domain.Employee;
import com.martikan.employeeapi.dto.EmployeeDTO;
import com.martikan.employeeapi.exception.BadRequestException;
import com.martikan.employeeapi.exception.NotFoundException;
import com.martikan.employeeapi.mapper.EmployeeMapper;
import com.martikan.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    void testGetAllEmployees_whenPageableGiven_thenReturnListOfDTOs() {
        final var pageable = Pageable.ofSize(20);
        final var emp1 = Faker.createRandomEmployee();
        final var emp2 = Faker.createRandomEmployee();
        final var empDTO1 = EmployeeDTO.builder()
            .id(emp1.getId())
            .email(emp1.getEmail())
            .firstName(emp1.getFirstName())
            .lastName(emp1.getLastName())
            .build();
        final var empDTO2 = EmployeeDTO.builder()
            .id(emp2.getId())
            .email(emp2.getEmail())
            .firstName(emp2.getFirstName())
            .lastName(emp2.getLastName())
            .build();
        final var employees = Stream.of(emp1, emp2);
        when(repository.findAllEmployee(any(Pageable.class))).thenReturn(employees);
        when(mapper.toDTO(emp1)).thenReturn(empDTO1);
        when(mapper.toDTO(emp2)).thenReturn(empDTO2);

        final var actualDTOs = service.getAllEmployees(pageable);

        assertNotNull(actualDTOs);
        assertEquals(2, actualDTOs.size());
        assertEquals(empDTO1, actualDTOs.get(0));
        assertEquals(empDTO2, actualDTOs.get(1));
        verify(repository, times(1)).findAllEmployee(any(Pageable.class));
        verify(mapper, times(2)).toDTO(any(Employee.class));
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void testGetAllEmployees_whenNoData_thenReturnEmptyList() {
        final var pageable = Pageable.ofSize(20);
        when(repository.findAllEmployee(any(Pageable.class))).thenReturn(Stream.empty());

        final var actualDTOs = service.getAllEmployees(pageable);

        assertNotNull(actualDTOs);
        assertEquals(0, actualDTOs.size());
        verify(repository, times(1)).findAllEmployee(any(Pageable.class));
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetEmployeeById_whenIdGiven_thenReturnDTO() {
        final var emp1 = Faker.createRandomEmployee();
        final var id = emp1.getId();
        final var empDTO1 = EmployeeDTO.builder()
            .id(id)
            .email(emp1.getEmail())
            .firstName(emp1.getFirstName())
            .lastName(emp1.getLastName())
            .build();
        when(repository.findById(emp1.getId())).thenReturn(Optional.of(emp1));
        when(mapper.toDTO(emp1)).thenReturn(empDTO1);

        final var actualDTO = service.getEmployeeById(id);

        assertNotNull(actualDTO);
        assertEquals(emp1.getId(), actualDTO.getId());
        assertEquals(emp1.getEmail(), actualDTO.getEmail());
        assertEquals(emp1.getFirstName(), actualDTO.getFirstName());
        assertEquals(emp1.getLastName(), actualDTO.getLastName());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toDTO(emp1);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void testGetEmployeeById_whenIdIsNotExist_thenThrowNotFoundException() {
        final var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getEmployeeById(id));
        verify(repository, times(1)).findById(id);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testUpdateEmployee_whenValidDTOAndIdGiven_thenExecuteUpdate() {
        final var employeeDTOForUpdate = Faker.createRandomEmployeeDTO();
        final var employeeForUpdate = new Employee();
        employeeForUpdate.setId(employeeDTOForUpdate.getId());
        employeeForUpdate.setEmail(employeeDTOForUpdate.getEmail());
        employeeForUpdate.setFirstName(employeeDTOForUpdate.getFirstName());
        employeeForUpdate.setLastName(employeeDTOForUpdate.getLastName());
        when(mapper.updateEntity(employeeDTOForUpdate, employeeForUpdate)).thenReturn(employeeForUpdate);
        when(repository.findById(employeeDTOForUpdate.getId())).thenReturn(Optional.of(employeeForUpdate));
        when(repository.save(employeeForUpdate)).thenReturn(employeeForUpdate);

        assertDoesNotThrow(() -> service.updateEmployee(employeeDTOForUpdate));
        verify(mapper, times(1)).updateEntity(employeeDTOForUpdate, employeeForUpdate);
        verify(repository, times(1)).findById(employeeDTOForUpdate.getId());
        verify(repository, times(1)).save(employeeForUpdate);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void testUpdateEmployee_whenIdIsNotExist_thenThrowNotFoundException() {
        final var employeeDTOForUpdate = Faker.createRandomEmployeeDTO();
        when(repository.findById(employeeDTOForUpdate.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateEmployee(employeeDTOForUpdate));
        verify(repository, times(1)).findById(employeeDTOForUpdate.getId());
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testSaveEmployee_whenValidDTOGiven_thenExecuteInsert() {
        final var employeeDTOForSave = Faker.createRandomEmployeeDTO();
        employeeDTOForSave.setId(null);
        final var employeeForSave = new Employee();
        employeeForSave.setEmail(employeeDTOForSave.getEmail());
        employeeForSave.setFirstName(employeeDTOForSave.getFirstName());
        employeeForSave.setLastName(employeeDTOForSave.getLastName());
        final var savedEmployee = new Employee();
        savedEmployee.setId(UUID.randomUUID());
        savedEmployee.setEmail(employeeForSave.getEmail());
        savedEmployee.setFirstName(employeeForSave.getFirstName());
        savedEmployee.setLastName(employeeForSave.getLastName());
        when(mapper.toEntity(employeeDTOForSave)).thenReturn(employeeForSave);
        when(repository.existsEmployeeByEmail(employeeDTOForSave.getEmail())).thenReturn(false);
        when(repository.save(employeeForSave)).thenReturn(savedEmployee);

        assertDoesNotThrow(() -> service.saveEmployee(employeeDTOForSave));
        verify(repository, times(1)).existsEmployeeByEmail(employeeDTOForSave.getEmail());
        verify(repository, times(1)).save(employeeForSave);
        verify(mapper, times(1)).toEntity(employeeDTOForSave);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void testSaveEmployee_whenIdIsNotNull_thenThrowBadRequestException() {
        final var employeeDTOForSave = Faker.createRandomEmployeeDTO();

        assertThrows(BadRequestException.class, () -> service.saveEmployee(employeeDTOForSave));
        verifyNoInteractions(mapper);
        verifyNoInteractions(repository);
    }

    @Test
    void testSaveEmployee_whenEmailIsAlreadyExist_thenThrowBadRequestException() {
        final var employeeDTOForSave = Faker.createRandomEmployeeDTO();
        employeeDTOForSave.setId(null);
        final var employeeForSave = new Employee();
        employeeForSave.setEmail(employeeDTOForSave.getEmail());
        employeeForSave.setFirstName(employeeDTOForSave.getFirstName());
        employeeForSave.setLastName(employeeDTOForSave.getLastName());
        when(repository.existsEmployeeByEmail(employeeDTOForSave.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.saveEmployee(employeeDTOForSave));
        verify(repository, times(1)).existsEmployeeByEmail(employeeDTOForSave.getEmail());
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testDeleteEmployeeById_whenIdGiven_thenExecuteDelete() {
        final var emp1 = Faker.createRandomEmployee();
        final var id = emp1.getId();
        when(repository.findById(id)).thenReturn(Optional.of(emp1));
        doNothing().when(repository).deleteById(id);

        service.deleteEmployeeById(id);

        verify(repository, times(1)).deleteById(id);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testDeleteEmployeeById_whenIdIsNotFound_thenThrowNotFoundException() {
        final var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteEmployeeById(id));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).deleteById(id);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }
}