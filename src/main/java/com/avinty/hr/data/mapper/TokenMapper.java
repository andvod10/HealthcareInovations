package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;

public class TokenMapper {
    public static Token toSaveEntity(Employee employee, String accessToken, String refreshToken) {
        return Token.builder()
                .employee(employee)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
