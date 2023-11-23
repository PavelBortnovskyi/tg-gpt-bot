package com.neo.gpt_bot_test.containers.db.service;

import com.neo.gpt_bot_test.containers.db.utils.ChatMessageFactory;
import com.neo.gpt_bot_test.enums.ChatMessageType;
import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.model.ChatMessage;
import com.neo.gpt_bot_test.repository.BotUserRepository;
import com.neo.gpt_bot_test.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
@Service
@RequiredArgsConstructor
public class ChatMessageService extends GeneralService<ChatMessage> {

    private final ChatMessageRepository chatMessageRepository;

    private final BotUserRepository botUserRepository;

    private final ChatMessageFactory chatMessageFactory;

    private final Bot bot;

    public Page<ChatMessage> getBotUserMessages(Long userId, Pageable pageable) {
        return chatMessageRepository.getBotUserMessages(userId, pageable);
    }

    public ResponseEntity<String> sendMessageToAll(ChatMessage chatMessage) {
        List<BotUser> botUsers = botUserRepository.findAll();
        if (!botUsers.isEmpty()) {
            botUsers.forEach(u -> {
                ChatMessage messageToUser = chatMessageFactory.createMessage(chatMessage.getBody(), u, ChatMessageType.ADMIN);
                u.getMessages().add(messageToUser);
                bot.sendTextAnswer(u.getChatId(), messageToUser.getBody(), null);
            });
            botUserRepository.saveAll(botUsers);

            return ResponseEntity.ok(String.format("Message sent to all bot users (%d)", botUsers.size()));
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry but users list is empty");
    }

    public ResponseEntity<String> sendMessageToUser(Long userId, ChatMessage chatMessage) {
        if (botUserRepository.existsById(userId)) {
            BotUser currUser = botUserRepository.getUserWithMessagesById(userId).get();
            ChatMessage messageToUser = chatMessageFactory.createMessage(chatMessage.getBody(), currUser, ChatMessageType.ADMIN);
            currUser.getMessages().add(messageToUser);
            bot.sendTextAnswer(currUser.getChatId(), messageToUser.getBody(), null);
            return ResponseEntity.ok(String.format("Message sent to user with id %d", userId));
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("User with id %d not exist", userId));
    }
}
