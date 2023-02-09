package com.avinty.hr.service.employees;

import com.avinty.hr.configuration.jwt.TokenProvider;
import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;
import com.avinty.hr.data.mapper.EmployeeMapper;
import com.avinty.hr.data.repository.DepartmentRepository;
import com.avinty.hr.data.repository.EmployeeRepository;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RsEmployee;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public non-sealed class EmployeesServiceImpl implements EmployeesService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    EmployeesServiceImpl(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
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
    public RsEmployeeInfo addAdminEmployee(RqEmployee rqEmployee) {
        validate(rqEmployee);
        String password = this.passwordEncoder.encode(rqEmployee.getPassword());
        Employee admin = this.employeeRepository.save(EmployeeMapper.toSaveAdminEntity(rqEmployee, password));
        Token token = this.tokenProvider.generateAccessAndRefreshTokens(admin);
        return RsEmployeeInfo.builder()
                .id(admin.getId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    @Override
    @Transactional
    public RsEmployeeInfo addEmployee(RqEmployee rqEmployee) {
        validate(rqEmployee);
        String password = this.passwordEncoder.encode(rqEmployee.getPassword());
        Department department = null;
        if (rqEmployee.getDepartmentId() != null) {
            department = this.departmentRepository.findById(rqEmployee.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException(rqEmployee.getDepartmentId()));
        }
        Employee employee = this.employeeRepository.save(EmployeeMapper.toSaveEmployeeEntity(rqEmployee, password, department));
        Token token = this.tokenProvider.generateAccessAndRefreshTokens(employee);
        return RsEmployeeInfo.builder()
                .id(employee.getId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    @Override
    @Transactional
    public void changeDepartment(String employeeId, String departmentId) {
        Employee employee = this.employeeRepository.findByIdFetch(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(employeeId));
        Department department = this.departmentRepository.findByIdFetch(departmentId)
                .orElseThrow(() -> new EntityNotFoundException(departmentId));
        if (employee.getDepartment() != null) {
            employee.getDepartment().removeEmployee(employee);
        }
        department.addEmployee(employee);
        log.info("Employee: {}", employee);
        log.info("Department: {}", department);
    }

    private void validate(RqEmployee rqEmployee) {
        if (this.employeeRepository.findByEmailIgnoreCase(rqEmployee.getEmail()).isPresent()) {
            throw new BadCredentialsException("Employee with email " + rqEmployee.getEmail() + " already exist!");
        }
    }
}
