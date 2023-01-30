package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.presentation.dto.RqDepartment;
import com.avinty.hr.presentation.dto.RsDepartment;
import com.avinty.hr.presentation.dto.RsDepartmentInfo;
import com.avinty.hr.presentation.dto.RsEmployee;

import static com.avinty.hr.service.employees.util.CustomDateTimeFormatter.formatDateTime;

import java.util.List;

public class DepartmentMapper {
    public static Department toSaveEntity(RqDepartment rqDepartment, Employee manager) {
        var department = new Department();
        department.setName(rqDepartment.getName());
        department.setManager(manager);
        return department;
    }

    public static RsDepartment toResponse(Department department, List<RsEmployee> employees) {
        return RsDepartment.builder()
                .id(department.getId())
                .createdBy(department.getCreatedBy() != null ? department.getCreatedBy().getId() : null)
                .createdAt(formatDateTime(department.getCreatedDate()))
                .updatedBy(department.getLastModifiedBy() != null ? department.getLastModifiedBy().getId() : null)
                .updatedAt(formatDateTime(department.getLastModifiedDate()))
                .name(department.getName())
                .managerId(department.getManager().getId())
                .employees(employees)
                .build();
    }

    public static RsDepartmentInfo toResponse(Department department) {
        return RsDepartmentInfo.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }

}
