package com.avinty.hr.configuration.jwt;

import com.avinty.hr.configuration.UserDetailsServiceImpl;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;
import com.avinty.hr.data.mapper.TokenMapper;
import com.avinty.hr.data.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenProvider {
    private final String base64Secret;
    private final Long SEC = 1000L;
    @Value("${jwt.access-token-lifetime-in-seconds}")
    private final Long accessTokenLifetimeInSeconds = 3600L;
    @Value("${jwt.refresh-token-lifetime-in-seconds}")
    private final Long refreshTokenLifetimeInSeconds = accessTokenLifetimeInSeconds * 48 * SEC;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenRepository tokenRepository;

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final Long accessTokenLifetimeMs = accessTokenLifetimeInSeconds * SEC;
    private final Long refreshTokenLifetimeMs = refreshTokenLifetimeInSeconds;
    private Key key;

    @Autowired
    TokenProvider(
            @Value("${jwt.base64-secret}")
                    String base64Secret,
            UserDetailsServiceImpl userDetailsService,
            TokenRepository tokenRepository
    ) {
        this.base64Secret = base64Secret;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token generateAccessAndRefreshTokens(Employee employee) {
        String accessToken = generateToken(employee, TokenType.ACCESS);
        String refreshToken = generateToken(employee, TokenType.REFRESH);
        return this.tokenRepository.save(TokenMapper.toSaveEntity(employee, accessToken, refreshToken));
    }

    public String generateToken(Employee employee, TokenType tokenType) {
        Long expirationTimeInEpoch = tokenType.equals(TokenType.ACCESS) ? accessTokenLifetimeMs : refreshTokenLifetimeMs;

        Date createdDate = new Date(Instant.now().toEpochMilli());
        Date expirationDate = new Date(createdDate.getTime() + expirationTimeInEpoch);

        return Jwts.builder()
                .setIssuer(employee.getId())
                .setSubject(employee.getId())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String authToken) {
        String userId = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
                .getBody()
                .getIssuer();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Boolean validateToken(String authToken) {
        try {
            return StringUtils.isNotBlank(Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken)
                    .getBody()
                    .getIssuer());
        } catch (RuntimeException e) {
            log.warn(e.getLocalizedMessage());
        }
        return false;
    }
}
