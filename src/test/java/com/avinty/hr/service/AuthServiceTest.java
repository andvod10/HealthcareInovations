package com.avinty.hr.service;

import com.avinty.hr.configuration.TestConfigurations;
import com.avinty.hr.configuration.jwt.TokenProvider;
import com.avinty.hr.data.entity.AccountRoles;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;
import com.avinty.hr.data.repository.EmployeeRepository;
import com.avinty.hr.data.repository.TokenRepository;
import com.avinty.hr.presentation.dto.RqEmployee;
import com.avinty.hr.presentation.dto.RqLogin;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;
import com.avinty.hr.service.employees.AuthService;
import com.avinty.hr.service.employees.EmployeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {TestConfigurations.class})
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class AuthServiceTest {
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TokenProvider tokenProvider;

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
    void loginAdminSuccessful() {
        Optional<Employee> optionalAdmin = this.employeeRepository.findByIdFetch(adminId);
        assertThat(optionalAdmin.isPresent()).isTrue();
        Employee admin = optionalAdmin.get();

        Optional<Token> optionalToken = this.tokenRepository.findByEmployee(admin);
        assertThat(optionalToken.isPresent()).isTrue();
        Token token = optionalToken.get();

        assertThat(token.getAccessToken()).isNotNull();

        RsEmployeeInfo rsEmployeeInfo = this.authService.login(RqLogin.builder()
                .email(admin.getEmail())
                .password("password")
                .build());

        Optional<Token> optionalUpdatedToken = this.tokenRepository.findByEmployee(admin);
        assertThat(optionalUpdatedToken.isPresent()).isTrue();
        Token updatedToken = optionalUpdatedToken.get();

        assertThat(updatedToken.getAccessToken()).isNotNull();
        assertThat(updatedToken.getAccessToken()).isEqualTo(rsEmployeeInfo.getAccessToken());
    }

    @Test
    void loginAdminReturnCorrectToken() {
        Optional<Employee> optionalAdmin = this.employeeRepository.findByIdFetch(adminId);
        assertThat(optionalAdmin.isPresent()).isTrue();
        Employee admin = optionalAdmin.get();

        RsEmployeeInfo rsEmployeeInfo = this.authService.login(RqLogin.builder()
                .email(admin.getEmail())
                .password("password")
                .build());

        assertThat(this.tokenProvider.getAuthentication(rsEmployeeInfo.getAccessToken()).getAuthorities().stream().map(GrantedAuthority::getAuthority))
                .contains(AccountRoles.ROLE_ADMIN.name());
    }

    @Test
    void loginEmployeeSuccessful() {
        Optional<Employee> optionalEmployee = this.employeeRepository.findByIdFetch(employeeId);
        assertThat(optionalEmployee.isPresent()).isTrue();
        Employee employee = optionalEmployee.get();

        Optional<Token> optionalToken = this.tokenRepository.findByEmployee(employee);
        assertThat(optionalToken.isPresent()).isTrue();
        Token token = optionalToken.get();

        assertThat(token.getAccessToken()).isNotNull();

        RsEmployeeInfo rsEmployeeInfo = this.authService.login(RqLogin.builder()
                .email(employee.getEmail())
                .password("password")
                .build());

        Optional<Token> optionalUpdatedToken = this.tokenRepository.findByEmployee(employee);
        assertThat(optionalUpdatedToken.isPresent()).isTrue();
        Token updatedToken = optionalUpdatedToken.get();

        assertThat(updatedToken.getAccessToken()).isNotNull();
        assertThat(updatedToken.getAccessToken()).isEqualTo(rsEmployeeInfo.getAccessToken());
    }

    @Test
    void loginEmployeeReturnCorrectToken() {
        Optional<Employee> optionalEmployee = this.employeeRepository.findByIdFetch(employeeId);
        assertThat(optionalEmployee.isPresent()).isTrue();
        Employee admin = optionalEmployee.get();

        RsEmployeeInfo rsEmployeeInfo = this.authService.login(RqLogin.builder()
                .email(admin.getEmail())
                .password("password")
                .build());

        assertThat(this.tokenProvider.getAuthentication(rsEmployeeInfo.getAccessToken()).getAuthorities().stream().map(GrantedAuthority::getAuthority))
                .contains(AccountRoles.ROLE_USER.name());
    }
}
