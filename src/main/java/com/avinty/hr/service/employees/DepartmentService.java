package com.avinty.hr.service.employees;

import com.avinty.hr.presentation.dto.RqDepartment;
import com.avinty.hr.presentation.dto.RsDepartment;
import com.avinty.hr.presentation.dto.RsDepartmentInfo;

import java.util.List;

public interface DepartmentService {
    List<RsDepartment> getAllDepartments();
    List<RsDepartmentInfo> getAllDepartmentsByName(String name);
    void deleteDepartment(String id);
    String addDepartment(RqDepartment rqDepartment);
}
