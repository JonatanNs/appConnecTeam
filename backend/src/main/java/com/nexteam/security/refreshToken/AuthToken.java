package com.nexteam.security.refreshToken;

import com.nexteam.common.BaseEntity;
import com.nexteam.features.Users.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthToken extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String token;
    private String refreshToken;
    private Boolean revokend;
    private Instant createdAt;
    private Instant expiresAt;

}


