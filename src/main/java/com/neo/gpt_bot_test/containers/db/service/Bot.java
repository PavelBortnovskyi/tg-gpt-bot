package com.neo.gpt_bot_test.containers.db.service;
import com.neo.gpt_bot_test.commands.Actions;
import com.neo.gpt_bot_test.containers.db.config.BotConfig;
import com.neo.gpt_bot_test.containers.db.config.BotStateKeeper;
import com.neo.gpt_bot_test.enums.Language;
import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.model.ChatMessage;
import com.neo.gpt_bot_test.repository.BotUserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;


@Log4j2
@Component
public class Bot extends TelegramLongPollingCommandBot {

    public List<BotCommand> botCommands;
    private final BotConfig botConfig;
    private final BotUserRepository botUserRepository;
    private final BotStateKeeper botStateKeeper;


    public Bot(BotConfig botConfig, BotUserRepository botUserRepository, BotStateKeeper botStateKeeper) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.botCommands = new ArrayList<>();
        this.botUserRepository = botUserRepository;
        this.botStateKeeper = botStateKeeper;
        Locale.setDefault(new Locale("en"));
        botCommands.add(new BotCommand("/start", "get started"));
        botCommands.add(new BotCommand("/my_data", "get info about user"));
        botCommands.add(new BotCommand("/delete_my_data", "remove all info about user"));
        botCommands.add(new BotCommand("/help", "get full commands list"));
        try {
            this.execute(new SetMyCommands(this.botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error while set bot`s command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            IBotCommand command = getRegisteredCommand(text);
            if (Objects.nonNull(command)) {
                command.processMessage(this, message, new String[]{});
            } else {
               Optional<BotUser> maybeCurrUser = botUserRepository.getUserWithMessagesByChatId(update.getMessage().getChatId());
               if (maybeCurrUser.isPresent()) {
                   BotUser currUser = maybeCurrUser.get();
                   ChatMessage freshMessage = new ChatMessage();
                   freshMessage.setUser(currUser);
                   freshMessage.setBody(update.getMessage().getText());
                   freshMessage.setCreatedAt(LocalDateTime.now());
                   freshMessage.setCreatedBy(currUser.getNickName());
                   freshMessage.setAuthorIsAdmin(false);
                   freshMessage.setAuthorIsAi(false);
                   currUser.getMessages().add(freshMessage);
                   botUserRepository.save(currUser);
               }
            }
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            BotUser currUser = botUserRepository.findByChatId(callbackQuery.getMessage().getChatId()).get();
            String answer = "";
            switch (callbackQuery.getData()) {
                case Actions.ENG_LANGUAGE -> {
                    currUser.setLanguage(Language.EN.toString());
                    answer = "Great! Lets continue on english!";
                }
                case Actions.UA_LANGUAGE -> {
                    currUser.setLanguage(Language.UA.toString());
                    answer = "Чудово! Продовжимо на солов'їній!";
                }
            }

            botUserRepository.save(currUser);

            try {
                sendApiMethod(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text(answer)
                        .build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            LocalizationManager.setLocale(new Locale(currUser.getLanguage().toLowerCase()));

            try {
                sendApiMethod(SendMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .text(MessageFormat.format(LocalizationManager.getString("start_message"), currUser.getFirstName()))
                        .build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void executeCommand(String commandName, String argument, Chat chat) {
        IBotCommand command = getRegisteredCommand(commandName);
        Message message = new Message();
        message.setText("/" + commandName + " " + argument);
        message.setChat(chat);
        String[] arg = new String[1];
        arg[0] = argument;
        command.processMessage(this, message, arg);
    }

    public void sendTextAnswer(long chatId, String answer, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(EmojiParser.parseToUnicode(answer))
                .build();
        if (keyboardMarkup != null)
            message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Got some TelegramAPI exception: " + e.getMessage());
        }
    }

    private void sendPhotoAnswer(long chatId, InputFile photo, ReplyKeyboardMarkup keyboardMarkup) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(photo)
                .build();
        if (keyboardMarkup != null)
            sendPhoto.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Got some TelegramAPI exception: " + e.getMessage());
        }
    }
}
