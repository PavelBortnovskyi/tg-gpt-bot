//package com.neo.gpt_bot_test.containers.db.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class OpenAIRestTemplateConfig {
//
//    @Value("${open_ai_api_key2}")
//    private String openaiApiKey;
//
//    @Bean
//    @Qualifier("openaiRestTemplate")
//    public RestTemplate openaiRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add((request, body, execution) -> {
//            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
//            return execution.execute(request, body);
//        });
//        return restTemplate;
//    }
//}