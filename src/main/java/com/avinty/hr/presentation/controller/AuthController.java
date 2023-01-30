package com.avinty.hr.presentation.controller;

import com.avinty.hr.presentation.APIVersions;
import com.avinty.hr.presentation.dto.RqLogin;
import com.avinty.hr.presentation.dto.RsEmployeeInfo;
import com.avinty.hr.service.employees.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIVersions.API + "/" + APIVersions.V1 + "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    RsEmployeeInfo login(@RequestBody RqLogin rqLogin) {
        return this.authService.login(rqLogin);
    }
}
