package com.martikan.employeeapi.controller;

import com.martikan.employeeapi.Routes;
import com.martikan.employeeapi.dto.ApiResponse;
import com.martikan.employeeapi.dto.EmployeeDTO;
import com.martikan.employeeapi.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(Routes.EMPLOYEE_ROUTE_V1)
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees(final Pageable pageable) {
        log.info("called - get /api/v1/employees");
        final var employees = employeeService.getAllEmployees(pageable);
        final var res = new ApiResponse<>(HttpStatus.OK.name(), employees);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable final UUID id) {
        log.info("called - get /api/v1/employees/{}", id.toString());
        final var employee = employeeService.getEmployeeById(id);
        final var res = new ApiResponse<>(HttpStatus.OK.name(), employee);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateEmployee(@PathVariable final UUID id,
                                                              @Valid @RequestBody final EmployeeDTO dto) {
        log.info("called - put /api/v1/employees/{}", id.toString());
        dto.setId(id);
        employeeService.updateEmployee(dto);
        final var res = new ApiResponse<>(HttpStatus.NO_CONTENT.name(), "Employee has been updated");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveEmployee(@Valid @RequestBody final EmployeeDTO dto) {
        log.info("called - post /api/v1/employees");
        employeeService.saveEmployee(dto);
        final var res = new ApiResponse<>(HttpStatus.CREATED.name(), "Employee has been created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable final UUID id) {
        log.info("called - delete /api/v1/employees/{}", id.toString());
        employeeService.deleteEmployeeById(id);
        final var res = new ApiResponse<>(HttpStatus.NO_CONTENT.name(), "Employee has been deleted");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
    }

}
