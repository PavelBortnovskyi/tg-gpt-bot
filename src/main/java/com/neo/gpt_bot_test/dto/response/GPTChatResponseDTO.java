package com.neo.gpt_bot_test.dto.response;

import com.neo.gpt_bot_test.model.Choice;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GPTChatResponseDTO {

    private List<Choice> choices;
}

