package com.avinty.hr.configuration.jwt;

import com.avinty.hr.configuration.UserModelDetails;
import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;
import com.avinty.hr.data.mapper.TokenMapper;
import com.avinty.hr.data.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider implements InitializingBean {
    @Value("${jwt.base64-secret}")
    private final String base64Secret = "";
    private final Long SEC = 1000L;
    @Value("${jwt.access-token-lifetime-in-seconds}")
    private final Long accessTokenLifetimeInSeconds = 3600L;
    @Value("${jwt.refresh-token-lifetime-in-seconds}")
    private final Long refreshTokenLifetimeInSeconds = accessTokenLifetimeInSeconds * 48 * SEC;
    private final UserModelDetails userDetailsService;
    private final TokenRepository tokenRepository;

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final Long accessTokenLifetimeMs = accessTokenLifetimeInSeconds * SEC;
    private final Long refreshTokenLifetimeMs = refreshTokenLifetimeInSeconds;
    private Key key;

    @Autowired
    TokenProvider(UserModelDetails userDetailsService, TokenRepository tokenRepository) {
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
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
        Map<String, String> claims = new HashMap<>();
        String AUTHORITIES_KEY = "id";
        claims.put(AUTHORITIES_KEY, employee.getId());
        return generateTokenWithClaims(claims, employee.getId(), tokenType);
    }

    private String generateTokenWithClaims(Map<String, String> claims, String userId, TokenType tokenType) {
        Long expirationTimeLong = null;
        switch (tokenType) {
            case ACCESS:
                expirationTimeLong = accessTokenLifetimeMs;
                break;
            case REFRESH:
                expirationTimeLong = refreshTokenLifetimeMs;
                break;
            default:
                break;
        }

        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String userId = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken).getBody().getSubject();
            return true;
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
