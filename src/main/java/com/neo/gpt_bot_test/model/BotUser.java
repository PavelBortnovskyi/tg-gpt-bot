package com.neo.gpt_bot_test.model;

import com.neo.gpt_bot_test.enums.Language;
import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@SequenceGenerator(name = "custom_gen", sequenceName = "users_id_seq", allocationSize = 1)
public class BotUser extends BaseEntity {

    @Column(name = "tg_chat_id")
    private Long chatId;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "locale")
    private String language = Language.EN.toString(); //Default value

    @Column(name = "ai_temp_level")
    private Double temperature = 0.7; //Default level

    @Column(name = "ai_profile")
    private String aiProfile = "You are a helpful assistant."; //Default

    @Column(name = "isNewbie")
    private boolean isNewbie = true;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ChatMessage> messages = new HashSet<>();
}

