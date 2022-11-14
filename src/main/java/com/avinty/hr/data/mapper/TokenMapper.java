package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;

import java.time.LocalDateTime;

public class TokenMapper {
    public static Token toSaveEntity(Employee employee, String accessToken, String refreshToken) {
        LocalDateTime createdAt = LocalDateTime.now();
        return Token.builder()
                .createdBy(employee)
                .createdAt(createdAt)
                .employee(employee)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
