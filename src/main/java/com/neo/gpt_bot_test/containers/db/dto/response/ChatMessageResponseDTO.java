package com.neo.gpt_bot_test.containers.db.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Chat message response")
public class ChatMessageResponseDTO {

    private long chatMessageId;

    private boolean authorIsAi;

    private boolean authorIsAdmin;

    private String body;

    private long userId;
}
