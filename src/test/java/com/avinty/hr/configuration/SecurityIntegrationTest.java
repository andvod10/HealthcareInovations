package com.avinty.hr.configuration;

import com.avinty.hr.presentation.controller.DepartmentController;
import com.avinty.hr.presentation.controller.EmployeesController;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsDepartmentInfo;
import com.avinty.hr.presentation.dto.RsEmployee;
import com.avinty.hr.service.employees.EmployeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = {TestConfigurations.class})
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class SecurityIntegrationTest {
    @Autowired
    private EmployeesController employeesController;
    @Autowired
    private DepartmentController departmentController;
    @Autowired
    private EmployeesService employeesService;

    private String adminId = null;
    private String employeeId = null;

    @BeforeEach
    public void setup() {
        RqEmployee rqAdminEmployee = RqEmployee.builder()
                .email("admin@email.com")
                .fullName("admin")
                .password("password")
                .build();
        adminId = this.employeesService.addAdminEmployee(rqAdminEmployee).getId();
        RqEmployee rqEmployeeEmployee = RqEmployee.builder()
                .createdBy(adminId)
                .email("employee@email.com")
                .fullName("employee")
                .password("password")
                .build();
        employeeId = this.employeesService.addEmployee(rqEmployeeEmployee).getId();
    }

    @Test
    @WithMockUser(username = "john", roles = {"ADMIN"})
    public void givenRoleAdmin_whenCallGetEmployees_returnList() {
        SecurityContextHolder.getContext();
        List<RsEmployee> employees = this.employeesController.getEmployees();
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "john", roles = {"USER"})
    public void givenRoleUser_whenCallGetEmployees_returnException() {
        assertThatThrownBy(() -> this.employeesController.getEmployees()).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "john", roles = {"USER"})
    public void givenRoleAdmin_whenCallGetSystemDate_returnDate() {
        List<RsDepartmentInfo> departments = this.departmentController.getDepartmentsByName("empty");
        assertThat(departments).isEmpty();
    }

}
