package com.neo.gpt_bot_test.repository;

import com.neo.gpt_bot_test.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ChatMessageRepository extends RepositoryInterface<ChatMessage> {

    @Transactional
    @Query("select m from ChatMessage m where m.user.id = :userId")
    Page<ChatMessage> getBotUserMessages(@Param("userId") Long userId, Pageable pageable);
}
