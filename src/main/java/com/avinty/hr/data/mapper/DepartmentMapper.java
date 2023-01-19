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
        return Department.builder()
                .name(rqDepartment.getName())
                .manager(manager)
                .build();
    }

    public static RsDepartment toResponse(Department department, List<RsEmployee> employees) {
        return RsDepartment.builder()
                .id(department.getId().toString())
                .createdBy(department.getCreatedBy() != null ? department.getCreatedBy().getId().toString() : null)
                .createdAt(formatDateTime(department.getCreatedDate()))
                .updatedBy(department.getLastModifiedBy() != null ? department.getLastModifiedBy().getId().toString() : null)
                .updatedAt(formatDateTime(department.getLastModifiedDate()))
                .name(department.getName())
                .managerId(department.getManager().getId().toString())
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
