package com.martikan.employeeapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
@Validated
public class EmployeeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2562976269012037765L;

    private UUID id;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;
}
