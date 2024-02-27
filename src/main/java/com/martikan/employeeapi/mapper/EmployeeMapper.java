package com.martikan.employeeapi.mapper;

import com.martikan.employeeapi.common.BaseMapper;
import com.martikan.employeeapi.domain.Employee;
import com.martikan.employeeapi.dto.EmployeeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<Employee, EmployeeDTO> {
}
