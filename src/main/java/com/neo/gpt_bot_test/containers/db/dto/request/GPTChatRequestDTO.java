package com.neo.gpt_bot_test.containers.db.dto.request;

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

    public GPTChatRequestDTO(String model, String prompt, Double temperature) {
        this.model = model;
        this.n = 1;
        this.temperature = temperature;
        this.messages = new ArrayList<>();
        this.messages.add(new GPTMessage("user", prompt));
    }

}
