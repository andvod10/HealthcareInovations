package com.avinty.hr.presentation.controller;

import com.avinty.hr.presentation.APIVersions;
import com.avinty.hr.presentation.dto.RqChangeDepartment;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;
import com.avinty.hr.service.employees.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(APIVersions.API + "/" + APIVersions.V1 + "/employees")
public class EmployeesController {
    private final EmployeesService employeesService;

    @Autowired
    EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @PostMapping("employee")
    RsEmployeeInfo addEmployee(@Valid @RequestBody RqEmployee rqEmployee) {
        return this.employeesService.addEmployee(rqEmployee);
    }

    @PostMapping("admin")
    RsEmployeeInfo addAdminEmployee(@RequestBody RqEmployee rqEmployee) {
        return this.employeesService.addAdminEmployee(rqEmployee);
    }

    @GetMapping()
    List<RsEmployee> getEmployees() {
        return this.employeesService.getEmployees();
    }

    @PutMapping("department")
    void changeDepartment(@RequestBody RqChangeDepartment rqChangeDepartment) {
        this.employeesService.changeDepartment(rqChangeDepartment);
    }
}
