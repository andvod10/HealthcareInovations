package com.avinty.hr.service.employees;

import com.avinty.hr.presentation.dto.RqChangeDepartment;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;

import java.util.List;

public interface EmployeesService {
    List<RsEmployee> getEmployees();
    String addAdminEmployee(RqEmployee rqEmployee);
    String addEmployee(RqEmployee rqEmployee);
    void changeDepartment(RqChangeDepartment rqChangeDepartment);
}
