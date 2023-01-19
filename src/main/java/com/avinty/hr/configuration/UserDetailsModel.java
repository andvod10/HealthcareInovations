package com.avinty.hr.configuration;

import com.avinty.hr.data.entity.Employee;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public record UserDetailsModel(
        Employee employee,
        String email,
        String password,
        Collection<GrantedAuthority> authorities
) implements UserDetails {
    @Builder public UserDetailsModel {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return employee.getId().toString();
    }

    public Employee getUser() {
        return employee;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetailsModel that = (UserDetailsModel) o;

        return Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return employee != null ? employee.hashCode() : 0;
    }
}
