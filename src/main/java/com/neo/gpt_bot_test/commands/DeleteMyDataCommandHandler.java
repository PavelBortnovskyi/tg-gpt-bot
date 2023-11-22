package com.neo.gpt_bot_test.commands;

import com.neo.gpt_bot_test.containers.db.service.LocalizationManager;
import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.repository.BotUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;
import java.util.Optional;

@Log4j2
@Component
public class DeleteMyDataCommandHandler extends BotCommand {

    private final BotUserRepository botUserRepository;

    public DeleteMyDataCommandHandler(@Value(TextCommands.DELETE_MY_DATA) String commandIdentifier,
                                      @Value(TextCommands.DMD_DESCRIPTION) String description,
                                      BotUserRepository botUserRepository) {
        super(commandIdentifier, description);
        this.botUserRepository = botUserRepository;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        long chatId = chat.getId();
        SendMessage messageToSend = SendMessage.builder().chatId(chatId).text("").build();
        Optional<BotUser> maybeCurrUser = botUserRepository.findByChatId(chatId);
        if (maybeCurrUser.isPresent()) {
            LocalizationManager.setLocale(new Locale(maybeCurrUser.get().getLanguage()));
            botUserRepository.deleteById(maybeCurrUser.get().getId());
            messageToSend.setText(LocalizationManager.getString("delete_my_data_success"));
            log.info("Deleted data about user with chatID: " + chatId);
        } else
            messageToSend.setText(LocalizationManager.getString("not_registered_message"));
        try {
            absSender.execute(messageToSend);
        } catch (TelegramApiException e) {
            log.error("Got some error in delete my data block: " + e.getMessage());
        }
    }
}