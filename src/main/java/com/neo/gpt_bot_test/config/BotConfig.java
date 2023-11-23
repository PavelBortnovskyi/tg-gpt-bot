package com.neo.gpt_bot_test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;
import java.net.Proxy;


@Getter
@Configuration
@EnableScheduling
public class BotConfig {


    @Value("${bot_token}")
    private String botToken;

    @Value("${bot_name}")
    private String botName;

    @Bean(name = "botToken")
    public String getBotToken() {
        return this.botToken;
    }

    @Bean(name = "botName")
    public String getBotName() {
        return this.botName;
    }
}

