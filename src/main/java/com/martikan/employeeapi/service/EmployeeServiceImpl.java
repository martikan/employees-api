package com.martikan.employeeapi.service;

import com.martikan.employeeapi.dto.EmployeeDTO;
import com.martikan.employeeapi.exception.BadRequestException;
import com.martikan.employeeapi.exception.NotFoundException;
import com.martikan.employeeapi.mapper.EmployeeMapper;
import com.martikan.employeeapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper mapper;

    @Override
    public List<EmployeeDTO> getAllEmployees(final Pageable pageable) {
        return employeeRepository.findAllEmployee(pageable)
            .map(mapper::toDTO)
            .toList();
    }

    @Override
    public EmployeeDTO getEmployeeById(final UUID id) {
        return employeeRepository.findById(id)
            .map(mapper::toDTO)
            .orElseThrow(() -> new NotFoundException("Employee has been not found with the given id"));
    }

    @Override
    public void updateEmployee(final EmployeeDTO dto) {
        employeeRepository.findById(dto.getId())
            .ifPresentOrElse(e -> {
                final var employee = mapper.updateEntity(dto, e);
                employeeRepository.save(employee);
            }, () -> {throw new NotFoundException("Employee has been not found with the given id");});
    }

    @Override
    public void saveEmployee(final EmployeeDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("Employee Id must be null for saving");
        } else if (employeeRepository.existsEmployeeByEmail(dto.getEmail())) {
            throw new BadRequestException("Email is already exist");
        }
        employeeRepository.save(mapper.toEntity(dto));
    }

    @Override
    public void deleteEmployeeById(final UUID id) {
        employeeRepository.findById(id)
                .ifPresentOrElse(e -> employeeRepository.deleteById(id),
                    () -> {throw new NotFoundException("Employee has been not found with the given id");});
    }
}
