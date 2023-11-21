package com.neo.gpt_bot_test.repository;

import com.neo.gpt_bot_test.model.AdminUser;

import java.util.Optional;

public interface AdminUserRepository extends RepositoryInterface<AdminUser> {

    Optional<AdminUser> findByEmail(String email);

    Optional<AdminUser> findByRefreshToken(String refreshToken);
}
