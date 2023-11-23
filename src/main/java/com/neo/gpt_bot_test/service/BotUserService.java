package com.neo.gpt_bot_test.service;

import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class BotUserService extends GeneralService<BotUser> {

  private final BotUserRepository botUserRepository;

  public Optional<BotUser> getUser(Long id) {
    return botUserRepository.findById(id);
  }


//  public Page<BotUser> getAllBotUsers(Integer pageSize, Integer pageNumber) {return botUserRepository.getPageableBotUsers(Pageable.ofSize(pageSize).withPage(pageNumber));}


}
