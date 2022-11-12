package com.avinty.hr.configuration;

import com.avinty.hr.configuration.jwt.JWTFilter;
import com.avinty.hr.configuration.jwt.TokenProvider;
import com.avinty.hr.data.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ApplicationSecurityConfig {
    private final CorsFilter corsFilter;
    private final TokenProvider tokenProvider;
    private final UserAuthEntryPoint userAuthEntryPoint;
    private final TokenRepository tokenRepository;

    @Autowired
    ApplicationSecurityConfig(CorsFilter corsFilter, TokenProvider tokenProvider, UserAuthEntryPoint userAuthEntryPoint, TokenRepository tokenRepository) {
        this.corsFilter = corsFilter;
        this.tokenProvider = tokenProvider;
        this.userAuthEntryPoint = userAuthEntryPoint;
        this.tokenRepository = tokenRepository;
    }

    private final List<String> AUTH_REQUESTS = List.of(
            "/${ClientAPI.API}/${API.VERSION}/auth/**",
            "/${AdminAPI.ADMIN}/${API.VERSION}/auth/login",
            "/${AdminAPI.ADMIN}/${API.VERSION}/auth/signup",
            "/${ClientAPI.API}/${API.VERSION}/payments/oauth/authorize",
            "/${ClientAPI.API}/${API.VERSION}/teams"
    );

    void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(String.valueOf(AUTH_REQUESTS)).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(
                        new JWTFilter(tokenProvider, tokenRepository),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling().authenticationEntryPoint(userAuthEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(String.valueOf(AUTH_REQUESTS));
    }
}
