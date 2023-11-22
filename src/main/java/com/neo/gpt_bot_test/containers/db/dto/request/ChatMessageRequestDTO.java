package com.neo.gpt_bot_test.containers.db.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import com.neo.gpt_bot_test.annotations.Marker;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "Chat message request")
public class ChatMessageRequestDTO {

    @JsonView({Marker.New.class})
    @NotNull(groups = {Marker.New.class})
    @ApiModelProperty(value = "message text", example = "Hi, everyone!", required = true, allowableValues = "range[1, 65535]")
    @Size(min = 1, max = 65535, message = "Text length must be in range 1..65535 characters")
    private String body;

    private boolean authorIsAdmin = true;
}
