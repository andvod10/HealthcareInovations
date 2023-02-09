package com.avinty.hr.configuration;

import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final EmployeeRepository employeeRepository;

    @Autowired
    UserDetailsServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) {
        log.debug("Authenticating user with id '{}'", id);

        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        return UserDetailsModel.builder()
                .employee(employee)
                .authorities(List.of(new SimpleGrantedAuthority(employee.getAccountRole().name())))
                .build();
    }
}
