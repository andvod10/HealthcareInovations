package com.avinty.hr.configuration.auditor;

import com.avinty.hr.configuration.UserDetailsModel;
import com.avinty.hr.data.entity.Employee;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<Employee> {

    @Override
    public Optional<Employee> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetailsModel principal) {
                return Optional.of(principal.getUser());
            }
        }
        return Optional.empty();
    }
}
