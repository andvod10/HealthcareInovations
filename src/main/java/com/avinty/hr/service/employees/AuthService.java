package com.avinty.hr.service.employees;

import com.avinty.hr.presentation.dto.RqLogin;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;

public sealed interface AuthService
    permits AuthServiceImpl {
    RsEmployeeInfo login(RqLogin rqLogin);
}
