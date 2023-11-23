package com.neo.gpt_bot_test.containers.db.config;

import com.neo.gpt_bot_test.enums.BotState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
@Getter
public class BotStateKeeper {

    private Map<Long, BotState> userStates = new HashMap<>();

    public BotState getStateForUser(Long userId) {
        return userStates.getOrDefault(userId, BotState.INITIALIZATION);
    }

    public void setStateForUser(Long userId, BotState botState) {
        userStates.put(userId, botState);
    }
}
