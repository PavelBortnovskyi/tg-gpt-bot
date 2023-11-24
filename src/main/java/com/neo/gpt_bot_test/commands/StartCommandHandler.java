package com.neo.gpt_bot_test.commands;

import com.neo.gpt_bot_test.config.BotStateKeeper;
import com.neo.gpt_bot_test.service.LocalizationManager;
import com.neo.gpt_bot_test.enums.BotState;
import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.repository.BotUserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Component
public class StartCommandHandler extends BotCommand {

    private final BotUserRepository botUserRepository;

    private final BotStateKeeper botStateKeeper;

    public StartCommandHandler(@Value(TextCommands.START) String commandIdentifier,
                               @Value(TextCommands.START_DESCRIPTION) String description,
                               BotUserRepository botUserRepository,
                               BotStateKeeper botStateKeeper) {
        super(commandIdentifier, description);
        this.botUserRepository = botUserRepository;
        this.botStateKeeper = botStateKeeper;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String greeting = MessageFormat.format(LocalizationManager.getString("choose_language_message"), chat.getFirstName());

        InlineKeyboardMarkup langOptions = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text(EmojiParser.parseToUnicode("English (EN) \uD83C\uDDEC\uD83C\uDDE7/\uD83C\uDDFA\uD83C\uDDF8"))
                                .callbackData(Actions.ENG_LANGUAGE)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(EmojiParser.parseToUnicode("Українська (UA) \uD83C\uDDFA\uD83C\uDDE6"))
                                .callbackData(Actions.UA_LANGUAGE)
                                .build()
                ))
                .build();

        SendMessage messageToSend = SendMessage.builder()
                .chatId(chat.getId())
                .text(greeting)
                .replyMarkup(langOptions)
                .build();

        registerUser(user, chat.getId());

        try {
            absSender.execute(messageToSend);
        } catch (TelegramApiException e) {
            System.out.println("Got some exception in start block: " + e.getMessage());
            log.error("Got some exception in start block: " + e.getMessage());
        }
    }

    private void registerUser(User user, long chatId) {
        if (this.botUserRepository.findByChatId(chatId).isEmpty()) {
            BotUser freshUser = new BotUser();
            freshUser.setChatId(chatId);
            freshUser.setNickName(user.getUserName());
            freshUser.setFirstName(user.getFirstName());
            freshUser.setLastName(user.getLastName());
            freshUser.setCreatedAt(LocalDateTime.now());
            freshUser.setCreatedBy(user.getUserName());
            log.info(String.format("New User with chatId: %d registered", freshUser.getChatId()));
        }
        botStateKeeper.setStateForUser(this.botUserRepository.findByChatId(chatId).get().getId(), BotState.INPUT_FOR_TEMPERATURE);
    }
}