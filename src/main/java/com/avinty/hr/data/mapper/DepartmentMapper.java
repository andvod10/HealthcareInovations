package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.presentation.dto.RqDepartment;
import com.avinty.hr.presentation.dto.RqEditDepartment;
import com.avinty.hr.presentation.dto.RsDepartment;
import com.avinty.hr.presentation.dto.RsDepartmentInfo;
import com.avinty.hr.presentation.dto.RsEmployee;
import static com.avinty.hr.service.employees.CustomDateTimeFormatter.formatDateTime;

import java.time.LocalDateTime;
import java.util.List;

public class DepartmentMapper {
    public static Department toSaveEntity(RqDepartment rqDepartment, Employee createdBy, Employee manager) {
        LocalDateTime createdAt = LocalDateTime.now();
        return Department.builder()
                .createdBy(createdBy)
                .createdAt(createdAt)
                .name(rqDepartment.getName())
                .manager(manager)
                .build();
    }

    public static RsDepartment toResponse(Department department, List<RsEmployee> employees) {
        return RsDepartment.builder()
                .id(department.getId())
                .createdBy(department.getCreatedBy() != null ? department.getCreatedBy().getId() : null)
                .createdAt(formatDateTime(department.getCreatedAt()))
                .updatedBy(department.getUpdatedBy() != null ? department.getUpdatedBy().getId() : null)
                .updatedAt(formatDateTime(department.getCreatedAt()))
                .name(department.getName())
                .managerId(department.getManager() != null ? department.getManager().getId() : null)
                .employees(employees)
                .build();
    }

    public static RsDepartmentInfo toResponse(Department department) {
        return RsDepartmentInfo.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }

    public static Department toUpdateEntity(RqEditDepartment rqEditDepartment, Employee updatedBy, Employee manager) {
        LocalDateTime updatedAt = LocalDateTime.now();
        return Department.builder()
                .id(rqEditDepartment.getId())
                .updatedBy(updatedBy)
                .updatedAt(updatedAt)
                .name(rqEditDepartment.getName())
                .manager(manager)
                .build();
    }
}
