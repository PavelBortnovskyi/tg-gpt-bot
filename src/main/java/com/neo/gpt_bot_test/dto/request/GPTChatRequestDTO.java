package com.neo.gpt_bot_test.dto.request;

import com.neo.gpt_bot_test.model.GPTMessage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GPTChatRequestDTO {

    private String model;
    private List<GPTMessage> messages;
    private int n;
    private double temperature;

    public GPTChatRequestDTO(String model, String profile, String prompt, Double temperature) {
        this.model = model;
        this.n = 1;
        this.temperature = temperature;
        this.messages = new ArrayList<>();
        this.messages.add(new GPTMessage("system", profile));
        this.messages.add(new GPTMessage("user", prompt));
    }

    public GPTChatRequestDTO(String model, String profile, String prompt, String previousUserPrompt, String previousAiAnswer, Double temperature) {
        this.model = model;
        this.n = 1;
        this.temperature = temperature;
        this.messages = new ArrayList<>();
        this.messages.add(new GPTMessage("system", profile));
        this.messages.add(new GPTMessage("user", previousUserPrompt));
        this.messages.add(new GPTMessage("assistant", previousAiAnswer));
        this.messages.add(new GPTMessage("user", prompt));
    }
}
