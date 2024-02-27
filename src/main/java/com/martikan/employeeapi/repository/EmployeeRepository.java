package com.martikan.employeeapi.repository;

import com.martikan.employeeapi.domain.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import java.util.stream.Stream;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("from Employee")
    Stream<Employee> findAllEmployee(final Pageable pageable);
    boolean existsEmployeeByEmail(final String email);
}
