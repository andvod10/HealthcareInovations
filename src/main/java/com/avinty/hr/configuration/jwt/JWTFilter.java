package com.avinty.hr.configuration.jwt;

import com.avinty.hr.data.entity.Token;
import com.avinty.hr.data.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JWTFilter extends GenericFilterBean {
    private final Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);
    private final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Autowired
    public JWTFilter(TokenProvider tokenProvider, TokenRepository tokenRepository) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
                Token authToken = tokenRepository.findToken(authentication.getName(), jwt)
                        .orElse(null);

            if (authToken == null) {
                LOGGER.debug("no valid JWT token found, uri: {}", requestURI);
                SecurityContextHolder.clearContext();
            } else {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                LOGGER.debug(
                        "Set Authentication to security context for '{}', uri: {}",
                        authentication.getName(),
                        requestURI
                );
            }
        } else {
            LOGGER.debug("no valid JWT token found, uri: {}", requestURI);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String prefix = "Bearer ";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(prefix.length());
        } else {
            return null;
        }
    }
}

