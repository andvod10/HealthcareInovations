package com.avinty.hr.service.employees;

import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.mapper.EmployeeMapper;
import com.avinty.hr.data.repository.DepartmentRepository;
import com.avinty.hr.data.repository.EmployeeRepository;
import com.avinty.hr.presentation.dto.RqChangeDepartment;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    EmployeesServiceImpl(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<RsEmployee> getEmployees() {
        return this.employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String addAdminEmployee(RqEmployee rqEmployee) {
        String password = this.passwordEncoder.encode(rqEmployee.getPassword());
        Employee creator = null;
        Department department = null;
        return this.employeeRepository.save(EmployeeMapper.toSaveEntity(rqEmployee, password, creator, department))
                .getId();
    }

    @Override
    @Transactional
    public String addEmployee(RqEmployee rqEmployee) {
        String password = this.passwordEncoder.encode(rqEmployee.getPassword());
        Employee creator = this.employeeRepository.findById(rqEmployee.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException(rqEmployee.getCreatedBy()));
        Department department = null;
        if (rqEmployee.getDepartmentId() != null) {
            department = this.departmentRepository.findById(rqEmployee.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException(rqEmployee.getDepartmentId()));
        }
        return this.employeeRepository.save(EmployeeMapper.toSaveEntity(rqEmployee, password, creator, department))
                .getId();
    }

    @Override
    @Transactional
    public void changeDepartment(RqChangeDepartment rqChangeDepartment) {
        Department department = null;
        Employee employee = this.employeeRepository.findById(rqChangeDepartment.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(rqChangeDepartment.getEmployeeId()));
        Employee updater = this.employeeRepository.findById(rqChangeDepartment.getUpdatedBy())
                .orElseThrow(() -> new EntityNotFoundException(rqChangeDepartment.getUpdatedBy()));
        if (rqChangeDepartment.getDepartmentId() != null) {
            department = this.departmentRepository.findById(rqChangeDepartment.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException(rqChangeDepartment.getDepartmentId()));
            department.getEmployees().add(employee);
        }
        employee.setDepartment(department);
        employee.setUpdatedBy(updater);
        employee.setUpdatedAt(LocalDateTime.now());
        this.employeeRepository.save(employee);
    }
}
