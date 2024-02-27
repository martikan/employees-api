package com.martikan.employeeapi.service;

import com.martikan.employeeapi.dto.EmployeeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees(final Pageable pageable);
    EmployeeDTO getEmployeeById(final UUID id);
    void updateEmployee(final EmployeeDTO dto);
    void saveEmployee(final EmployeeDTO dto);
    void deleteEmployeeById(final UUID id);
}
