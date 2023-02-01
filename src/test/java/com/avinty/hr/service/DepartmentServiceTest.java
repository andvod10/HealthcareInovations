package com.avinty.hr.service;

import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.repository.DepartmentRepository;
import com.avinty.hr.presentation.dto.RqDepartment;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsDepartment;
import com.avinty.hr.presentation.dto.RsDepartmentInfo;
import com.avinty.hr.service.employees.DepartmentService;
import com.avinty.hr.service.employees.EmployeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class DepartmentServiceTest {
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentRepository departmentRepository;

    private String adminId = null;
    private String departmentId = null;

    @BeforeEach
    public void setup() {
        RqEmployee rqAdminEmployee = RqEmployee.builder()
                .email("admin@email.com")
                .fullName("admin")
                .password("password")
                .build();
        adminId = this.employeesService.addAdminEmployee(rqAdminEmployee).getId();
        RqDepartment rqDepartment = RqDepartment.builder()
                .name("department")
                .managerId(adminId)
                .build();
        departmentId = this.departmentService.addDepartment(rqDepartment);
    }

    @Test
    void addDepartmentTest() {
        Optional<Department> optionalDepartment = this.departmentRepository.findByIdFetch(departmentId);
        assertThat(optionalDepartment.isPresent()).isTrue();
        Department department = optionalDepartment.get();

        assertThat(department.getName()).isEqualTo("department");
        assertThat(department.getManager().getId()).isEqualTo(adminId);
    }

    @Test
    void getAllDepartmentsTest() {
        RqDepartment rqDepartmentB = RqDepartment.builder()
                .name("departmentB")
                .managerId(adminId)
                .build();
        String departmentIdB = this.departmentService.addDepartment(rqDepartmentB);

        List<RsDepartment> departments = this.departmentService.getAllDepartments();
        assertThat(departments.size()).isEqualTo(2);
        assertThat(departments.stream().map(RsDepartment::getId).collect(Collectors.toList())).contains(departmentId, departmentIdB);
    }

    @Test
    void getAllDepartmentsByNameTest() {
        RqDepartment rqDepartmentC = RqDepartment.builder()
                .name("departmentC")
                .managerId(adminId)
                .build();
        String departmentIdC = this.departmentService.addDepartment(rqDepartmentC);

        List<RsDepartmentInfo> departments = this.departmentService.getAllDepartmentsByName("departmentC");
        assertThat(departments.size()).isEqualTo(1);
        assertThat(departments.stream().map(RsDepartmentInfo::getId).collect(Collectors.toList())).contains(departmentIdC);
    }

    @Test
    void deleteDepartmentTest() {
        assertThat(this.departmentRepository.findById(departmentId).isPresent()).isTrue();
        this.departmentService.deleteDepartment(departmentId);
        assertThat(this.departmentRepository.findById(departmentId).isEmpty()).isTrue();
    }
}
