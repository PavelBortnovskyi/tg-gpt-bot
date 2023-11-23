package com.neo.gpt_bot_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class GptBotTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GptBotTestApplication.class, args);
    }

}
