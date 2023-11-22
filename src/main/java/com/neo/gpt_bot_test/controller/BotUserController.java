package com.neo.gpt_bot_test.controller;

import com.neo.gpt_bot_test.containers.db.dto.response.BotUserResponseDTO;
import com.neo.gpt_bot_test.containers.db.facade.BotUserFacade;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Log4j2
@Validated
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(originPatterns = {"*"})
@RequiredArgsConstructor
public class BotUserController {

    private final BotUserFacade botUserFacade;

    /**
     * This endpoint for get pageable list of bot users
     */
    @ApiOperation("Get all bot users list (pageable)")
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<BotUserResponseDTO> handleGetBotUsers( @RequestParam("page") @NotNull Integer page,
                                                       @RequestParam("size") @NotNull @Positive Integer pageSize) {
        return botUserFacade.getAllBotUsers(pageSize, page);
    }
}

