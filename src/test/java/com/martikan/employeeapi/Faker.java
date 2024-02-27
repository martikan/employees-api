package com.martikan.employeeapi;

import com.martikan.employeeapi.domain.Employee;
import com.martikan.employeeapi.dto.EmployeeDTO;

import java.util.UUID;

public class Faker {

    private static final com.github.javafaker.Faker faker = new com.github.javafaker.Faker();

    public static Employee createRandomEmployee() {
        final var employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName(faker.name().firstName());
        employee.setLastName(faker.name().lastName());
        employee.setEmail(faker.internet().emailAddress());

        return employee;
    }

    public static EmployeeDTO createRandomEmployeeDTO() {
        return EmployeeDTO.builder()
            .id(UUID.randomUUID())
            .email(faker.internet().emailAddress())
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .build();
    }

}
