package com.neo.gpt_bot_test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GPTMessage {

    private String role;
    private String content;

    public GPTMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}

