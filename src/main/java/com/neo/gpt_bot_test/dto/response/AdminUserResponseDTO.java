package com.neo.gpt_bot_test.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Admin user response")
public class AdminUserResponseDTO {

    private String nickName;

    private String email;
}
