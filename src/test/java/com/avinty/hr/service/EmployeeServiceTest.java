package com.avinty.hr.service;

import com.avinty.hr.configuration.TestConfigurations;
import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.repository.DepartmentRepository;
import com.avinty.hr.data.repository.EmployeeRepository;
import com.avinty.hr.data.repository.TokenRepository;
import com.avinty.hr.presentation.dto.RqChangeDepartment;
import com.avinty.hr.presentation.dto.RqDepartment;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;
import com.avinty.hr.service.employees.util.CustomDateTimeFormatter;
import com.avinty.hr.service.employees.DepartmentService;
import com.avinty.hr.service.employees.EmployeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = {TestConfigurations.class})
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TokenRepository tokenRepository;

    private String adminId = null;
    private String employeeId = null;
    private String departmentId = null;

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
    @Transactional
    void addEmployeeTest() {
        Optional<Employee> optionalEmployee = this.employeeRepository.findByIdFetch(employeeId);
        assertThat(optionalEmployee.isPresent()).isTrue();
        Employee employee = optionalEmployee.get();

        assertThat(employee.getCreatedBy().getId()).isEqualTo(adminId);
        assertThat(employee.getCreatedAt()).isNotNull();
        assertThat(employee.getUpdatedBy()).isNull();
        assertThat(employee.getUpdatedAt()).isNotNull();
        assertThat(employee.getEmail()).isEqualTo("employee@email.com");
        assertThat(employee.getFullName()).isEqualTo("employee");
        assertThat(employee.getPassword()).isNotNull();
        assertThat(employee.getDepartment()).isNull();
    }

    @Test
    @Transactional
    void getAllEmployeesTest() {
        List<RsEmployee> employees = this.employeesService.getEmployees();
        assertThat(employees.size()).isEqualTo(2);
        assertThat(employees.stream().map(RsEmployee::id).collect(Collectors.toList())).contains(adminId, employeeId);
    }

    @Test
    @Transactional
    void employeeAlreadyExistTest() {
        RqEmployee rqEmployeeExisted = RqEmployee.builder()
                .createdBy(adminId)
                .email("Employee@email.com")
                .fullName("employee")
                .password("password")
                .build();
        assertThatThrownBy(() -> {
            this.employeesService.addEmployee(rqEmployeeExisted);
        }).isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Employee with email " + rqEmployeeExisted.getEmail() + " already exist!");
    }

    @Test
    @Transactional
    void changeDepartmentTest() throws InterruptedException {
        Optional<Employee> optionalEmployee = this.employeeRepository.findByIdFetch(employeeId);
        assertThat(optionalEmployee.isPresent()).isTrue();
        Employee employee = optionalEmployee.get();
        String createdAt = CustomDateTimeFormatter.formatDateTime(employee.getCreatedAt());
        String updatedAt = CustomDateTimeFormatter.formatDateTime(employee.getUpdatedAt());
        RqDepartment rqDepartment = RqDepartment.builder()
                .createdBy(adminId)
                .name("department")
                .managerId(adminId)
                .build();
        departmentId = this.departmentService.addDepartment(rqDepartment);
        RqChangeDepartment rqChangeDepartment = RqChangeDepartment.builder()
                .employeeId(employeeId)
                .updatedBy(adminId)
                .departmentId(departmentId)
                .build();

        //Wait for updateAt will be changed
        Thread.sleep(1000);
        this.employeesService.changeDepartment(rqChangeDepartment);
        RsEmployee updatedEmployee = this.employeesService.getEmployees().stream()
                .filter(it -> it.id().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(employeeId));

        assertThat(updatedEmployee.createdAt()).isEqualTo(createdAt);
        assertThat(updatedEmployee.updatedBy()).isEqualTo(adminId);
        assertThat(updatedEmployee.updatedAt()).isNotEqualTo(updatedAt);
        assertThat(updatedEmployee.email()).isEqualTo("employee@email.com");
        assertThat(updatedEmployee.departmentId()).isEqualTo(departmentId);
    }

    @Test
    void changeDepartmentEmployeeAddedToTheListTest() {
        RqDepartment rqDepartment = RqDepartment.builder()
                .createdBy(adminId)
                .name("department")
                .managerId(adminId)
                .build();
        departmentId = this.departmentService.addDepartment(rqDepartment);
        Optional<Department> optionalDepartment = this.departmentRepository.findByIdFetchAll(departmentId);
        assertThat(optionalDepartment.isPresent()).isTrue();
        Department department = optionalDepartment.get();
        assertThat(department.getEmployees()).isEmpty();

        RqChangeDepartment rqChangeDepartment = RqChangeDepartment.builder()
                .employeeId(employeeId)
                .updatedBy(adminId)
                .departmentId(departmentId)
                .build();
        this.employeesService.changeDepartment(rqChangeDepartment);

        Optional<Department> optionalDepartmentUpdated = this.departmentRepository.findByIdFetchAll(departmentId);
        assertThat(optionalDepartmentUpdated.isPresent()).isTrue();
        Department departmentUpdated = optionalDepartmentUpdated.get();
        assertThat(departmentUpdated.getEmployees().size()).isEqualTo(1);

        this.tokenRepository.deleteAll();
        this.departmentService.deleteDepartment(departmentId);
        this.employeeRepository.deleteById(employeeId);
        this.employeeRepository.deleteById(adminId);
    }
}
