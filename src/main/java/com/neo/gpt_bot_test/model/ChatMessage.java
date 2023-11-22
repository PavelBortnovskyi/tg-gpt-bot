package com.neo.gpt_bot_test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@Entity
@Getter
@Setter
@Table(name = "messages")
@NoArgsConstructor
@SequenceGenerator(name = "custom_gen", sequenceName = "messages_id_seq", allocationSize = 1)
public class ChatMessage extends BaseEntity {

    private boolean authorIsAi;

    private boolean authorIsAdmin;

    @Size(max = 65535)
    private String body;

    @ManyToOne(targetEntity = BotUser.class)
    @JoinColumn(name = "user_id")
    private BotUser user;
}
