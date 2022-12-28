package com.avinty.hr.service.employees;

import com.avinty.hr.presentation.dto.RqChangeDepartment;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;

import java.util.List;

public sealed interface EmployeesService
        permits EmployeesServiceImpl {
    List<RsEmployee> getEmployees();
    RsEmployeeInfo addAdminEmployee(RqEmployee rqEmployee);
    RsEmployeeInfo addEmployee(RqEmployee rqEmployee);
    void changeDepartment(RqChangeDepartment rqChangeDepartment);
}
