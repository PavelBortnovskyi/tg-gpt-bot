package com.neo.gpt_bot_test.repository;

import com.neo.gpt_bot_test.model.BotUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotUserRepository extends RepositoryInterface<BotUser> {
    public Optional<BotUser> findByChatId(long chatId);

    @Query("SELECT u FROM BotUser u LEFT JOIN FETCH u.messages WHERE u.chatId = :chatId") //FETCH for request optimization!
    Optional<BotUser> getUserWithMessagesByChatId(@Param("chatId") Long chatId);

    @Query("SELECT u FROM BotUser u LEFT JOIN FETCH u.messages WHERE u.id = :userId") //FETCH for request optimization!
    Optional<BotUser> getUserWithMessagesById(@Param("userId") Long userId);
}
