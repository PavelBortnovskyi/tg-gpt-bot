package com.neo.gpt_bot_test.security;

import com.neo.gpt_bot_test.containers.db.service.AdminUserService;
import com.neo.gpt_bot_test.exceptions.authError.EmailNotFoundException;
import com.neo.gpt_bot_test.model.AdminUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final AdminUserService adminUserService;

    public UserDetails mapper(AdminUser adminUser) {
        return User
                .withUsername(adminUser.getEmail())
                .password(adminUser.getPassword())
                .roles("ADMIN")
                .build();
    }

    /**
     * Method returns User Details object for Spring Security authentication procedure using user email as login parameter
     *
     * @throws EmailNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userMail) throws EmailNotFoundException {
        return this.adminUserService.getAdmin(userMail)
                .map(this::mapper)
                .orElseThrow(() -> new EmailNotFoundException(String.format("User with email: `%s` not found", userMail)
                ));
    }
}
