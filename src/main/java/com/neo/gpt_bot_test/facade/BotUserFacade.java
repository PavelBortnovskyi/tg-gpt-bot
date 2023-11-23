package com.neo.gpt_bot_test.facade;

import com.neo.gpt_bot_test.dto.request.BotUserRequestDTO;
import com.neo.gpt_bot_test.dto.response.BotUserResponseDTO;
import com.neo.gpt_bot_test.service.BotUserService;
import com.neo.gpt_bot_test.service.ServiceInterface;
import com.neo.gpt_bot_test.model.BotUser;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BotUserFacade extends GeneralFacade<BotUser, BotUserRequestDTO, BotUserResponseDTO>{

  private final BotUserService botUserService;

  public BotUserFacade(ModelMapper mm, ServiceInterface<BotUser> service, BotUserService botUserService) {
    super(mm, service);
    this.botUserService = botUserService;
  }

  public Page<BotUserResponseDTO> getAllBotUsers(Integer pageSize, Integer pageNumber) {
    return botUserService.findAllPageable(Pageable.ofSize(pageSize).withPage(pageNumber)).map(this::convertToDto);
  }
}

