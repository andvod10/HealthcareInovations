package com.avinty.hr.service.employees;

import com.avinty.hr.configuration.jwt.TokenProvider;
import com.avinty.hr.configuration.jwt.TokenType;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;
import com.avinty.hr.data.repository.EmployeeRepository;
import com.avinty.hr.data.repository.TokenRepository;
import com.avinty.hr.presentation.dto.RqLogin;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class AuthServiceImpl implements AuthService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Autowired
    AuthServiceImpl(
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider, TokenRepository tokenRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public RsEmployeeInfo login(RqLogin rqLogin) {
        Employee employee = this.employeeRepository.findByEmailIgnoreCase(rqLogin.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(rqLogin.getEmail()));
        if (!this.passwordEncoder.matches(rqLogin.getPassword(), employee.getPassword())) {
            throw new BadCredentialsException("Password for employee " + employee.getId() + "doesn't match");
        }
        Token token = this.tokenRepository.findByEmployee(employee)
                .orElse(Token.builder()
                        .employee(employee)
                        .build());

        String accessToken = this.tokenProvider.generateToken(employee, TokenType.ACCESS);
        token.setAccessToken(accessToken);
        return RsEmployeeInfo.builder()
                .id(employee.getId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
