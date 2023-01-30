package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.AccountRoles;
import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;

import static com.avinty.hr.service.employees.util.CustomDateTimeFormatter.formatDateTime;

public class EmployeeMapper {
    public static Employee toSaveAdminEntity(RqEmployee rqEmployee, String password) {
        var employee = new Employee();
        employee.setEmail(rqEmployee.getEmail());
        employee.setPassword(password);
        employee.setFullName(rqEmployee.getFullName());
        employee.setAccountRole(AccountRoles.ROLE_ADMIN);
        return employee;
    }

    public static Employee toSaveEmployeeEntity(RqEmployee rqEmployee, String password, Department department) {
        var employee = new Employee();
        employee.setEmail(rqEmployee.getEmail());
        employee.setPassword(password);
        employee.setFullName(rqEmployee.getFullName());
        employee.setDepartment(department);
        employee.setAccountRole(AccountRoles.ROLE_USER);
        return employee;
    }

    public static RsEmployee toResponse(Employee employee) {
        return new RsEmployee(
                employee.getId(),
                employee.getCreatedBy() != null ? employee.getCreatedBy().getId() : null,
                formatDateTime(employee.getCreatedDate()),
                employee.getLastModifiedBy() != null ? employee.getLastModifiedBy().getId() : null,
                formatDateTime(employee.getLastModifiedDate()),
                employee.getEmail(),
                employee.getFullName(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null
        );
    }
}
