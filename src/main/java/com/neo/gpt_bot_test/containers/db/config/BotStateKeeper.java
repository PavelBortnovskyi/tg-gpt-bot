package com.neo.gpt_bot_test.containers.db.config;

import com.neo.gpt_bot_test.enums.BotState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
public class BotStateKeeper {

    private BotState botState = BotState.INITIALIZATION;

    public void changeState(BotState botState) {
        this.botState = botState;
    }
}
