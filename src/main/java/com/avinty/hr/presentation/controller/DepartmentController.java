package com.avinty.hr.presentation.controller;

import com.avinty.hr.presentation.APIVersions;
import com.avinty.hr.presentation.dto.RqDepartment;
import com.avinty.hr.presentation.dto.RsDepartment;
import com.avinty.hr.presentation.dto.RsDepartmentInfo;
import com.avinty.hr.service.employees.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(APIVersions.API + "/" + APIVersions.V1 + "/")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("dep-emp")
    List<RsDepartment> getAllDepEmp() {
        return this.departmentService.getAllDepartments();
    }

    @GetMapping("department")
    List<RsDepartmentInfo> getDepartmentsByName(@RequestParam(value = "name") String name) {
        return this.departmentService.getAllDepartmentsByName(name);
    }

    @DeleteMapping("department/{id}")
    void deleteDepartment(@PathVariable(value = "id") String id) {
        this.departmentService.deleteDepartment(id);
    }

    @PostMapping("department")
    String addDepartment(@RequestBody RqDepartment rqDepartment) {
        return this.departmentService.addDepartment(rqDepartment);
    }
}
