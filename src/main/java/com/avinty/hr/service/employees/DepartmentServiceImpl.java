package com.avinty.hr.service.employees;

import com.avinty.hr.data.entity.Department;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.mapper.DepartmentMapper;
import com.avinty.hr.data.mapper.EmployeeMapper;
import com.avinty.hr.data.repository.DepartmentRepository;
import com.avinty.hr.data.repository.EmployeeRepository;
import com.avinty.hr.presentation.dto.RqDepartment;
import com.avinty.hr.presentation.dto.RsDepartment;
import com.avinty.hr.presentation.dto.RsDepartmentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public non-sealed class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<RsDepartment> getAllDepartments() {
        return this.departmentRepository.findAllByAndFetchEmployees()
                .stream()
                .map(dep -> DepartmentMapper.toResponse(dep, dep.getEmployees()
                        .stream()
                        .map(EmployeeMapper::toResponse)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<RsDepartmentInfo> getAllDepartmentsByName(String name) {
        return this.departmentRepository.findByName(name)
                .stream()
                .map(DepartmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDepartment(String id) {
        Department department = this.departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        department.getEmployees()
                .forEach(it -> it.setDepartment(null));
        this.departmentRepository.delete(department);
    }

    @Override
    @Transactional
    public String addDepartment(RqDepartment rqDepartment) {
        Employee createdBy = this.employeeRepository.findById(rqDepartment.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException(rqDepartment.getCreatedBy()));
        Employee manager = this.employeeRepository.findById(rqDepartment.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException(rqDepartment.getManagerId()));
        return this.departmentRepository.save(DepartmentMapper.toSaveEntity(rqDepartment, createdBy, manager))
                .getId();
    }
}
