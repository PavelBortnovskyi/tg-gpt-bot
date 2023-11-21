package com.neo.gpt_bot_test.model;

import com.neo.gpt_bot_test.containers.db.utils.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class BaseEntity extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_gen")
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;
}
