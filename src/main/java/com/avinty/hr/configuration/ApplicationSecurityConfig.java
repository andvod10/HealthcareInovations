package com.avinty.hr.configuration;

import com.avinty.hr.configuration.jwt.JWTFilter;
import com.avinty.hr.presentation.APIVersions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ApplicationSecurityConfig {
    private final CorsFilter corsFilter;
    private final UserAuthEntryPoint userAuthEntryPoint;
    private final JWTFilter jwtFilter;

    @Autowired
    ApplicationSecurityConfig(CorsFilter corsFilter, UserAuthEntryPoint userAuthEntryPoint, JWTFilter jwtFilter) {
        this.corsFilter = corsFilter;
        this.userAuthEntryPoint = userAuthEntryPoint;
        this.jwtFilter = jwtFilter;
    }

    private final String[] AUTH_REQUESTS = List.of(
            "/" + APIVersions.API + "/" + APIVersions.V1 + "/employees/admin",
            "/" + APIVersions.API + "/" + APIVersions.V1 + "/employees/employee",
            "/" + APIVersions.API + "/" + APIVersions.V1 + "/auth/**"
    ).toArray(String[]::new);

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.antMatchers(AUTH_REQUESTS).permitAll();
                    authorize.anyRequest().authenticated();
                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        ).exceptionHandling()
                .authenticationEntryPoint(userAuthEntryPoint);
        return http.build();
    }
}
