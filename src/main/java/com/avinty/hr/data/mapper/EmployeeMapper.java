package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.AccountRoles;
import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.presentation.dto.RqEditEmployee;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;

import java.time.LocalDateTime;

import static com.avinty.hr.service.employees.CustomDateTimeFormatter.formatDateTime;

public class EmployeeMapper {
    public static Employee toSaveAdminEntity(RqEmployee rqEmployee, String password) {
        LocalDateTime createdAt = LocalDateTime.now();
        return Employee.builder()
                .createdAt(createdAt)
                .email(rqEmployee.getEmail())
                .password(password)
                .fullName(rqEmployee.getFullName())
                .accountRole(AccountRoles.ROLE_ADMIN)
                .build();
    }

    public static Employee toSaveEmployeeEntity(RqEmployee rqEmployee, String password, Employee createdBy, Department department) {
        LocalDateTime createdAt = LocalDateTime.now();
        return Employee.builder()
                .createdBy(createdBy)
                .createdAt(createdAt)
                .email(rqEmployee.getEmail())
                .password(password)
                .fullName(rqEmployee.getFullName())
                .department(department)
                .accountRole(AccountRoles.ROLE_USER)
                .build();
    }

    public static RsEmployee toResponse(Employee employee) {
        return RsEmployee.builder()
                .id(employee.getId())
                .createdBy(employee.getCreatedBy() != null ? employee.getCreatedBy().getId() : null)
                .createdAt(formatDateTime(employee.getCreatedAt()))
                .updatedBy(employee.getUpdatedBy() != null ? employee.getUpdatedBy().getId() : null)
                .updatedAt(formatDateTime(employee.getUpdatedAt()))
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .build();
    }

    public static Employee toUpdateEntity(RqEditEmployee rqEditEmployee, String password, Employee updatedBy, Department department) {
        LocalDateTime updatedAt = LocalDateTime.now();
        return Employee.builder()
                .id(rqEditEmployee.getId())
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .email(rqEditEmployee.getEmail())
                .password(password)
                .fullName(rqEditEmployee.getFullName())
                .department(department)
                .build();
    }
}
