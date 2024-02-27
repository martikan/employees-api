package com.martikan.employeeapi.controller;

import com.martikan.employeeapi.EmployeeApiApplicationTests;
import com.martikan.employeeapi.Faker;
import com.martikan.employeeapi.Routes;
import com.martikan.employeeapi.domain.Employee;
import com.martikan.employeeapi.repository.EmployeeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmployeeControllerITest extends EmployeeApiApplicationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private final UUID notExistingId = UUID.randomUUID();

    private UUID existingId;

    private final Employee existingEmployee1 = Faker.createRandomEmployee();

    private final Employee existingEmployee2 = Faker.createRandomEmployee();

    private final Employee existingEmployee3 = Faker.createRandomEmployee();

    @BeforeEach
    void setup() {
        final var savedEmployee = employeeRepository.save(existingEmployee1);
        employeeRepository.save(existingEmployee2);
        employeeRepository.save(existingEmployee3);
        existingId = savedEmployee.getId();
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        employeeRepository.flush();
    }

    @Test
    @SneakyThrows
    void testGetAllEmployees_whenPageableGiven_thenReturnEmployeesWithStatusOK() {
        mockMvc.perform(get(Routes.EMPLOYEE_ROUTE_V1)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(HttpStatus.OK.name())))
            .andExpect(jsonPath("$.message", hasSize(3)))
            .andExpect(jsonPath("$.message.[*].email",
                everyItem(oneOf(existingEmployee1.getEmail(), existingEmployee2.getEmail(), existingEmployee3.getEmail()))));
    }

    @Test
    @SneakyThrows
    void testGetEmployeeById_whenValidIdGiven_thenReturnEmployee() {
        mockMvc.perform(get(String.format("%s/%s", Routes.EMPLOYEE_ROUTE_V1, existingId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(HttpStatus.OK.name())))
            .andExpect(jsonPath("$.message.id", is(existingId.toString())))
            .andExpect(jsonPath("$.message.email", is(existingEmployee1.getEmail())))
            .andExpect(jsonPath("$.message.firstName", is(existingEmployee1.getFirstName())))
            .andExpect(jsonPath("$.message.lastName", is(existingEmployee1.getLastName())));
    }

    @Test
    @SneakyThrows
    void testGetEmployeeById_whenNotExistsIdGiven_thenThrowNotFoundException() {
        mockMvc.perform(get(String.format("%s/%s", Routes.EMPLOYEE_ROUTE_V1, notExistingId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
            .andExpect(jsonPath("$.message", is("Employee has been not found with the given id")));
    }

    @Test
    @SneakyThrows
    void testUpdateEmployee_whenValidDataGiven_thenUpdateEmployee() {
        final var employeeForUpdate = Faker.createRandomEmployeeDTO();
        employeeForUpdate.setId(existingId);

        mockMvc.perform(put(String.format("%s/%s", Routes.EMPLOYEE_ROUTE_V1, existingId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeForUpdate)))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.status", is(HttpStatus.NO_CONTENT.name())))
            .andExpect(jsonPath("$.message", is("Employee has been updated")));
    }

    @Test
    @SneakyThrows
    void testUpdateEmployee_whenNotExistsIdGiven_thenThrowNotFoundException() {
        final var employeeForUpdate = Faker.createRandomEmployeeDTO();
        employeeForUpdate.setId(notExistingId);

        mockMvc.perform(get(String.format("%s/%s", Routes.EMPLOYEE_ROUTE_V1, notExistingId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
            .andExpect(jsonPath("$.message", is("Employee has been not found with the given id")));
    }

    @Test
    @SneakyThrows
    void testSaveEmployee_whenEmailIsNotExistAndIdIsNull_thenSaveEmployee() {
        final var employeeForSave = Faker.createRandomEmployeeDTO();
        employeeForSave.setId(null);

        mockMvc.perform(post(Routes.EMPLOYEE_ROUTE_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeForSave)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status", is(HttpStatus.CREATED.name())))
            .andExpect(jsonPath("$.message", is("Employee has been created")));
    }

    @Test
    @SneakyThrows
    void testSaveEmployee_whenIdIsNotNull_thenThrowBadRequestException() {
        final var employeeForSave = Faker.createRandomEmployeeDTO();

        mockMvc.perform(post(Routes.EMPLOYEE_ROUTE_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeForSave)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
            .andExpect(jsonPath("$.message", is("Employee Id must be null for saving")));
    }

    @Test
    @SneakyThrows
    void testSaveEmployee_whenEmailIsExist_thenThrowBadRequestException() {
        final var employeeForSave = Faker.createRandomEmployeeDTO();
        employeeForSave.setId(null);
        employeeForSave.setEmail(existingEmployee1.getEmail());

        mockMvc.perform(post(Routes.EMPLOYEE_ROUTE_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeForSave)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
            .andExpect(jsonPath("$.message", is("Email is already exist")));
    }

    @Test
    @SneakyThrows
    void testDeleteEmployee_whenIdIsExist_thenDeleteEmployee() {
        mockMvc.perform(delete(String.format("%s/%s", Routes.EMPLOYEE_ROUTE_V1, existingId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.status", is(HttpStatus.NO_CONTENT.name())))
            .andExpect(jsonPath("$.message", is("Employee has been deleted")));
    }

    @Test
    @SneakyThrows
    void testDeleteEmployee_whenIdIsNotExist_thenThrowNotFoundException() {
        mockMvc.perform(delete(String.format("%s/%s", Routes.EMPLOYEE_ROUTE_V1, notExistingId))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
            .andExpect(jsonPath("$.message", is("Employee has been not found with the given id")));
    }
}