package com.digiteo.neovoteIV.security.jpa;

import com.digiteo.neovoteIV.model.jpa.data.BaseEntity;
import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "secureTokens")
public class SecureToken extends BaseEntity {

    @Column(unique = true)
    private String token;

    @Column(
            name = "creation_timestamp",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime timestamp;

    @Column(
            name = "expiration_timestamp",
            updatable = false
    )
    @Basic(optional = false)
    private LocalDateTime expireAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    @Transient
    private boolean isExpired;
}
