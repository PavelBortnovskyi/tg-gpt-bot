package com.neo.gpt_bot_test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "admins")
@SequenceGenerator(name = "custom_gen", sequenceName = "admins_id_seq", allocationSize = 1)
public class AdminUser extends BaseEntity {

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_expired")
    private boolean expired = false;
}
