package com.neo.gpt_bot_test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "messages")
@NoArgsConstructor
@SequenceGenerator(name = "custom_gen", sequenceName = "messages_id_seq", allocationSize = 1)
public class ChatMessage extends BaseEntity {

    private boolean authorIsAi;

    private boolean authorIsAdmin;

    private String body;

    @ManyToOne(targetEntity = BotUser.class)
    @JoinColumn(name = "user_id")
    private BotUser user;
}
