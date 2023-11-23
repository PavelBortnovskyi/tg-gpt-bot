package com.neo.gpt_bot_test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Choice {

    private int index;
    private GPTMessage message;

    public Choice(int index, GPTMessage message) {
        this.index = index;
        this.message = message;
    }
}
