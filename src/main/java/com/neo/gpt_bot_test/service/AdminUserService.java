package com.neo.gpt_bot_test.service;

import com.neo.gpt_bot_test.model.AdminUser;
import com.neo.gpt_bot_test.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminUserService extends GeneralService<AdminUser> {

    private final AdminUserRepository adminUserRepository;

    private final PasswordEncoder encoder;

    public Optional<AdminUser> getAdmin(Long id) {
        return adminUserRepository.findById(id);
    }

    public Optional<AdminUser> getAdmin(String email) {
        return adminUserRepository.findByEmail(email);
    }


    public Optional<AdminUser> getAdminByRefreshToken(String refreshToken) {
        return adminUserRepository.findByRefreshToken(refreshToken);
    }

    public boolean checkRefreshTokenStatus(String refreshToken) {
        return getAdminByRefreshToken(refreshToken).map(AdminUser::isExpired).orElse(false);
    }

    @Transactional
    public void updateRefreshTokenById(Long userId, String refreshToken) {
        getAdmin(userId).get().setRefreshToken(refreshToken);
    }

    @Transactional
    public void changeRefreshTokenStatusById(Long userId, boolean usedStatus) {
        getAdmin(userId).get().setExpired(usedStatus);
    }

    @Transactional
    public void changeTokenStatusByValue(String token, boolean status) {
        getAdminByRefreshToken(token).get().setExpired(status);
    }

    /**
     * Method returns boolean result of updating admin password operation (after checking login&password combination) and updates it in case right combination
     */
    @Transactional
    public boolean updatePassword(String email, String oldPassword, String freshPassword) {
        return getAdmin(email)
                .filter(admin -> encoder.matches(oldPassword, admin.getPassword()))
                .map(admin -> {
                    admin.setPassword(freshPassword);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Method returns boolean result of checking presence in DB user with login&password combination
     */
    public boolean checkLoginPassword(String email, String password) {
        return getAdmin(email)
                .map(admin -> encoder.matches(password, admin.getPassword()))
                .orElse(false);
    }

    /**
     * Method returns true if provided email address is present in DB
     */
    public boolean isEmailPresentInDB(String email) {
        return adminUserRepository.findByEmail(email).isPresent();
    }
}
