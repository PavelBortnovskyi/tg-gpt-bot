package com.neo.gpt_bot_test.containers.db.service;

import com.neo.gpt_bot_test.model.ChatMessage;
import com.neo.gpt_bot_test.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatMessageService extends GeneralService<ChatMessage> {

  private final ChatMessageRepository chatMessageRepository;

}
