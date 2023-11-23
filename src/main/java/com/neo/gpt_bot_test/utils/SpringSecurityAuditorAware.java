package com.neo.gpt_bot_test.utils;

import com.neo.gpt_bot_test.security.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private static final ThreadLocal<String> currentAuditor = new ThreadLocal<>();

    @NotNull
    public Optional<String> getCurrentAuditor() {
        log.info("Got auditor: " + getUserDetails().getUsername());
        return Optional.ofNullable(getUserDetails().getUsername());
    }

    private JwtUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails)
            return (JwtUserDetails) authentication.getPrincipal();
        else return new JwtUserDetails(0L, "Zero@mail.net");
    }
}
