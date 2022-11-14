package com.avinty.hr.service.employees;

import com.avinty.hr.presentation.dto.RqLogin;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;

public interface AuthService {
    RsEmployeeInfo login(RqLogin rqLogin);
}
