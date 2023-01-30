package com.avinty.hr.data.mapper;

import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;

public class TokenMapper {
    public static Token toSaveEntity(Employee employee, String accessToken, String refreshToken) {
        var token = new Token();
        token.setEmployee(employee);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        return token;
    }
}
