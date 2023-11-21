package com.neo.gpt_bot_test.repository;

import com.neo.gpt_bot_test.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryInterface<E extends BaseEntity> extends JpaRepository<E, Long> {
}