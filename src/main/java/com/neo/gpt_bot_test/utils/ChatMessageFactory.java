package com.neo.gpt_bot_test.utils;

import com.neo.gpt_bot_test.enums.ChatMessageType;
import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.model.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatMessageFactory {

    @Value("${openai_model}")
    private String model;

    public ChatMessage createMessage(String body, BotUser user, ChatMessageType chatMessageType) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setBody(body);
        chatMessage.setUser(user);
        switch (chatMessageType) {
            case AI -> {
                chatMessage.setCreatedBy(model);
                chatMessage.setAuthorIsAi(true);
                chatMessage.setAuthorIsAdmin(false);
            }
            case ADMIN -> {
                chatMessage.setAuthorIsAdmin(true);
                chatMessage.setAuthorIsAi(false);
                chatMessage.setBody("ADMIN: " + body);
            }
            case USER -> {
                chatMessage.setCreatedBy(user.getNickName());
                chatMessage.setCreatedAt(LocalDateTime.now());
                chatMessage.setAuthorIsAdmin(false);
                chatMessage.setAuthorIsAi(false);
            }
        }
        return chatMessage;
    }
}
