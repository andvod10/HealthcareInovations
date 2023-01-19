package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.AccountRoles;
import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;

import static com.avinty.hr.service.employees.util.CustomDateTimeFormatter.formatDateTime;

public class EmployeeMapper {
    public static Employee toSaveAdminEntity(RqEmployee rqEmployee, String password) {
        return Employee.builder()
                .email(rqEmployee.getEmail())
                .password(password)
                .fullName(rqEmployee.getFullName())
                .accountRole(AccountRoles.ROLE_ADMIN)
                .build();
    }

    public static Employee toSaveEmployeeEntity(RqEmployee rqEmployee, String password, Department department) {
        return Employee.builder()
                .email(rqEmployee.getEmail())
                .password(password)
                .fullName(rqEmployee.getFullName())
                .department(department)
                .accountRole(AccountRoles.ROLE_USER)
                .build();
    }

    public static RsEmployee toResponse(Employee employee) {
        return new RsEmployee(
            employee.getId().toString(),
            employee.getCreatedBy() != null ? employee.getCreatedBy().getId().toString() : null,
            formatDateTime(employee.getCreatedDate()),
            employee.getLastModifiedBy() != null ? employee.getLastModifiedBy().getId().toString() : null,
            formatDateTime(employee.getLastModifiedDate()),
            employee.getEmail(),
            employee.getFullName(),
            employee.getDepartment() != null ? employee.getDepartment().getId().toString() : null
        );
    }
}
