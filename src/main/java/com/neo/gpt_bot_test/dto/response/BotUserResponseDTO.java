package com.neo.gpt_bot_test.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(description = "Bot user response")
public class BotUserResponseDTO {

    private long id;

    private Long chatId;

    private String nickName;

    private String firstName;

    private String lastName;
}
