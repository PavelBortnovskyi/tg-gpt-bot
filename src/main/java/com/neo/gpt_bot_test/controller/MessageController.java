package com.neo.gpt_bot_test.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.neo.gpt_bot_test.annotations.Marker;
import com.neo.gpt_bot_test.dto.request.ChatMessageRequestDTO;
import com.neo.gpt_bot_test.dto.response.ChatMessageResponseDTO;
import com.neo.gpt_bot_test.facade.ChatFacade;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Log4j2
@Validated
@RestController
@RequestMapping("/api/v1/messages")
@CrossOrigin(originPatterns = {"*"})
@RequiredArgsConstructor
public class MessageController {

    private final ChatFacade chatFacade;

    /**
     * This endpoint for get pageable list of messages
     */
    @ApiOperation("Get all messages list (pageable)")
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ChatMessageResponseDTO> handleGetAllMessages(@RequestParam("page") @NotNull Integer page,
                                                             @RequestParam("size") @NotNull @Positive Integer pageSize) {
        return chatFacade.getAllMessages(pageSize, page);
    }

    /**
     * This endpoint for get pageable list of user messages
     */
    @ApiOperation("Get all messages list (pageable)")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ChatMessageResponseDTO> handleGetUserMessages(@PathVariable("id") Long id,
                                                              @RequestParam("page") @NotNull Integer page,
                                                              @RequestParam("size") @NotNull @Positive Integer pageSize) {
        return chatFacade.getUserMessages(id, pageSize, page);
    }

    /**
     * This endpoint to send messages for all registered bot users
     */
    @ApiOperation("Send message to all users")
    @PostMapping(path = "/send_all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleSendToAll(@RequestBody @JsonView({Marker.New.class}) @Valid ChatMessageRequestDTO messageDTO) {
        return chatFacade.sendMessageToAll(messageDTO);
    }

    /**
     * This endpoint to send messages for bot user with id (not chat_id!)
     */
    @ApiOperation("Send message to user")
    @PostMapping(path = "/send/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleSendToUser(@PathVariable("id") Long id,
                                                   @RequestBody @JsonView({Marker.New.class}) @Valid ChatMessageRequestDTO messageDTO) {
        return chatFacade.sendMessageToUser(id, messageDTO);
    }
}

