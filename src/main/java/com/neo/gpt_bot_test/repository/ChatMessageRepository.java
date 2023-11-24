package com.neo.gpt_bot_test.repository;

import com.neo.gpt_bot_test.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ChatMessageRepository extends RepositoryInterface<ChatMessage> {

    @Transactional
    @Query("select m from ChatMessage m where m.user.id = :userId order by m.createdAt asc")
    Page<ChatMessage> getBotUserMessages(@Param("userId") Long userId, Pageable pageable);

    @Transactional
    @Query("select m from ChatMessage m order by m.createdAt asc")
    Page<ChatMessage> getAllMessages(Pageable pageable);
}
