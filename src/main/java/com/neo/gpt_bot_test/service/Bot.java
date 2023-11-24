package com.neo.gpt_bot_test.service;

import com.neo.gpt_bot_test.commands.Actions;
import com.neo.gpt_bot_test.config.BotConfig;
import com.neo.gpt_bot_test.config.BotStateKeeper;
import com.neo.gpt_bot_test.utils.ChatMessageFactory;
import com.neo.gpt_bot_test.enums.BotState;
import com.neo.gpt_bot_test.enums.ChatMessageType;
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
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;


@Log4j2
@Component
public class Bot extends TelegramLongPollingCommandBot {

    public List<BotCommand> botCommands;
    private final BotConfig botConfig;
    private final BotUserRepository botUserRepository;
    private final BotStateKeeper botStateKeeper;
    private final OpenAiClient openAiClient;
    private final ChatMessageFactory chatMessageFactory;

    private String userPromptCache = "";

    private String aiAnswerCache = "";

    public Bot(BotConfig botConfig, BotUserRepository botUserRepository, BotStateKeeper botStateKeeper,
               OpenAiClient openAiClient, ChatMessageFactory chatMessageFactory) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.botCommands = new ArrayList<>();
        this.botUserRepository = botUserRepository;
        this.botStateKeeper = botStateKeeper;
        this.openAiClient = openAiClient;
        this.chatMessageFactory = chatMessageFactory;
        Locale.setDefault(new Locale("en"));
        botCommands.add(new BotCommand("/start", "get started"));
        botCommands.add(new BotCommand("/my_data", "get info about user"));
        botCommands.add(new BotCommand("/delete_my_data", "remove all info about user"));
        botCommands.add(new BotCommand("/help", "get full commands list"));
        botCommands.add(new BotCommand("/set_temperature", "set AI model creativity level"));
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

                    switch (botStateKeeper.getStateForUser(currUser.getId())) {
                        case INPUT_FOR_GPT -> {
                            if (text.equalsIgnoreCase("Glory to Robots!") || text.equalsIgnoreCase("Слава роботам!")) {
                                sendAnimatedAnswer(currUser.getChatId(), new InputFile(new File("src/main/resources/im-so-great-bender.mp4")), null);
                            } else {
                                ChatMessage userMessage = chatMessageFactory.createMessage(text, currUser, ChatMessageType.USER);
                                ChatMessage aiResponse;
                                if (userPromptCache.isEmpty() && aiAnswerCache.isEmpty())
                                    aiResponse = openAiClient.getAiAnswer(text, null, null, currUser);
                                else
                                    aiResponse = openAiClient.getAiAnswer(text, aiAnswerCache, userPromptCache, currUser);

                                currUser.getMessages().add(userMessage);
                                currUser.getMessages().add(aiResponse);

                                sendTextAnswer(currUser.getChatId(), aiResponse.getBody(), null);
                                botUserRepository.save(currUser);
                                userPromptCache = text;
                                aiAnswerCache = aiResponse.getBody();
                            }
                        }
                        case INPUT_FOR_TEMPERATURE -> {
                            try {
                                Double temperature = Double.parseDouble(update.getMessage().getText());
                                if (temperature >= 0 && temperature <= 2)
                                    currUser.setTemperature(temperature);
                                else throw new NumberFormatException();

                                if (currUser.isNewbie()) {
                                    sendTextAnswer(currUser.getChatId(), LocalizationManager.getString("start_message"), null);
                                    currUser.setNewbie(false);
                                } else
                                    sendTextAnswer(currUser.getChatId(), MessageFormat.format(LocalizationManager.getString("temperature_set_confirm"), currUser.getTemperature()), null);

                                botUserRepository.save(currUser);

                                botStateKeeper.setStateForUser(currUser.getId(), BotState.INPUT_FOR_GPT);
                            } catch (NumberFormatException e) {
                                sendTextAnswer(currUser.getChatId(), LocalizationManager.getString("temp_wrong_input"), null);
                            }
                        }
                    }
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
            LocalizationManager.setLocale(new Locale(currUser.getLanguage().toLowerCase()));

            try {
                sendApiMethod(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text(answer)
                        .build());

                sendApiMethod(SendMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .text(LocalizationManager.getString("temperature_option_message"))
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
            log.error("Got some TelegramAPI exception in text answer block: " + e.getMessage());
        }
    }

    private void sendAnimatedAnswer(long chatId, InputFile animation, ReplyKeyboardMarkup keyboardMarkup) {
        SendAnimation sendAnimation = SendAnimation.builder()
                .animation(animation)
                .chatId(chatId)
                .build();
        if (keyboardMarkup != null)
            sendAnimation.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendAnimation);
        } catch (TelegramApiException e) {
            log.error("Got some TelegramAPI exception in send animation block: " + e.getMessage());
        }
    }
}
