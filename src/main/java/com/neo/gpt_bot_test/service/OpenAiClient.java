package com.neo.gpt_bot_test.service;

import com.neo.gpt_bot_test.dto.request.GPTChatRequestDTO;
import com.neo.gpt_bot_test.dto.response.GPTChatResponseDTO;
import com.neo.gpt_bot_test.utils.ChatMessageFactory;
import com.neo.gpt_bot_test.enums.ChatMessageType;
import com.neo.gpt_bot_test.model.BotUser;
import com.neo.gpt_bot_test.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Log4j2
@Component
@RequiredArgsConstructor
public class OpenAiClient {

    @Value("${open_ai_api_key}")
    private String openaiApiKey;

    @Value("${openai_model}")
    private String model;

    @Value("${openai_api_url}")
    private String apiUrl;

    private final ChatMessageFactory chatMessageFactory;


    public ChatMessage getAiAnswer(String prompt, String previousAiAnswer, String previousUserPrompt, BotUser botUser) {
        GPTChatRequestDTO request;
        if (previousAiAnswer !=null && previousUserPrompt !=null) {
            request = new GPTChatRequestDTO(model, botUser.getAiProfile(), prompt, previousUserPrompt, previousAiAnswer, botUser.getTemperature());
        } else request = new GPTChatRequestDTO(model, botUser.getAiProfile(), prompt, botUser.getTemperature());
        GPTChatResponseDTO response = getOpenAiRestTemplate().postForObject(apiUrl, request, GPTChatResponseDTO.class);

        ChatMessage chatMessage;

        if (!response.getChoices().isEmpty()) {
            chatMessage = chatMessageFactory.createMessage(response.getChoices().get(0).getMessage().getContent(), botUser, ChatMessageType.AI);
        } else
            chatMessage = chatMessageFactory.createMessage("Sorry but AI can`t answer now", botUser, ChatMessageType.AI);
        return chatMessage;
    }

    private RestTemplate getOpenAiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
