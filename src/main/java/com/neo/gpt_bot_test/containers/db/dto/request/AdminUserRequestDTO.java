package com.neo.gpt_bot_test.containers.db.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import com.neo.gpt_bot_test.annotations.Marker;
import com.neo.gpt_bot_test.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "Admin user request")
public class AdminUserRequestDTO extends BaseEntity {

    @JsonView({Marker.New.class})
    @NotNull(groups = {Marker.New.class})
    @ApiModelProperty(value = "Nickname", example = "Bulbazaur", required = true, allowableValues = "range[2, 20]")
    @Size(max = 20, min = 2, message = "Nickname length must be in range 2..20 characters")
    private String nickName;

    @JsonView({Marker.New.class, Marker.Existed.class})
    @NotNull(groups = {Marker.New.class, Marker.Existed.class})
    @ApiModelProperty(value = "Email", example = "putin.die@example.com", required = true, allowableValues = "range[6, 50]")
    @Size(min = 6, max = 50, message = "Email length must be in range 6..50 characters")
    @Pattern(regexp = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$", message = "Invalid email format")
    private String email;

    @JsonView({Marker.New.class, Marker.Existed.class})
    @NotNull(groups = {Marker.New.class, Marker.Existed.class})
    @ApiModelProperty(value = "Password", example = "password123", required = true, allowableValues = "range[8, 50]")
    @Size(min = 8, max = 50, message = "Password length must be in range 8..50 characters")
    private String password;
}
