package com.neo.gpt_bot_test.facade;

import com.neo.gpt_bot_test.dto.request.ChatMessageRequestDTO;
import com.neo.gpt_bot_test.dto.response.ChatMessageResponseDTO;
import com.neo.gpt_bot_test.service.ChatMessageService;
import com.neo.gpt_bot_test.service.ServiceInterface;
import com.neo.gpt_bot_test.model.ChatMessage;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ChatFacade extends GeneralFacade<ChatMessage, ChatMessageRequestDTO, ChatMessageResponseDTO> {

    private final ChatMessageService chatMessageService;

    public ChatFacade(ModelMapper mm, ServiceInterface<ChatMessage> service, ChatMessageService chatMessageService) {
        super(mm, service);
        this.chatMessageService = chatMessageService;
    }

    public Page<ChatMessageResponseDTO> getAllMessages(Integer pageSize, Integer pageNumber) {
        return chatMessageService.findAllPageable(Pageable.ofSize(pageSize).withPage(pageNumber)).map(this::convertToDto);
    }

    public Page<ChatMessageResponseDTO> getUserMessages(Long userId, Integer pageSize, Integer pageNumber) {
        return chatMessageService.getBotUserMessages(userId, Pageable.ofSize(pageSize).withPage(pageNumber)).map(this::convertToDto);
    }

    public ResponseEntity<String> sendMessageToAll(ChatMessageRequestDTO messageRequestDTO) {
        return chatMessageService.sendMessageToAll(convertToEntity(messageRequestDTO));
    }

    public ResponseEntity<String> sendMessageToUser(Long userId, ChatMessageRequestDTO messageRequestDTO) {
        return chatMessageService.sendMessageToUser(userId, convertToEntity(messageRequestDTO));
    }
}

