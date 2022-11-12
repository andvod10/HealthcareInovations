package com.avinty.hr.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserModelDetails implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserModelDetails.class);

    @Override
    public UserDetails loadUserByUsername(String id) {
        log.debug("Authenticating user '{}'", id);

        return UserDetailsImpl.builder()
                .id(id)
                .build();
    }
}
