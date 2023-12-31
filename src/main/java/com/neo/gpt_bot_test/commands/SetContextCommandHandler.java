package com.neo.gpt_bot_test.commands;

import com.neo.gpt_bot_test.config.BotStateKeeper;
import com.neo.gpt_bot_test.enums.BotState;
import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.repository.BotUserRepository;
import com.neo.gpt_bot_test.service.LocalizationManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;


@Log4j2
@Component
public class SetContextCommandHandler extends BotCommand {

    private final BotUserRepository botUserRepository;

    private final BotStateKeeper botStateKeeper;

    public SetContextCommandHandler(@Value(TextCommands.SET_AI_CONTEXT) String commandIdentifier,
                                    @Value(TextCommands.SET_AI_CONTEXT_DESCRIPTION) String description,
                                    BotUserRepository botUserRepository,
                                    BotStateKeeper botStateKeeper) {
        super(commandIdentifier, description);
        this.botUserRepository = botUserRepository;
        this.botStateKeeper = botStateKeeper;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String message;
        Optional<BotUser> maybeCurrUser = botUserRepository.findByChatId(chat.getId());
        if (maybeCurrUser.isPresent()) {
            botStateKeeper.setStateForUser(maybeCurrUser.get().getId(), BotState.INPUT_FOR_CONTEXT);
            message = LocalizationManager.getString("context_option_message");
        } else {
            message = LocalizationManager.getString("not_registered_message");
        }

        SendMessage messageToSend = SendMessage.builder()
                .chatId(chat.getId())
                .text(message)
                .build();
        try {
            absSender.execute(messageToSend);
        } catch (TelegramApiException e) {
            log.error("Got some exception in set context block: " + e.getMessage());
        }
    }
}