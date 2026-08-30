package com.nexteam.security.refreshToken;

import com.nexteam.features.Users.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByRefreshToken(String refreshToken);

    void deleteByUser_Id(Long userId);

    List<Token> findAllByUser(User user);

    @Modifying
    @Query("UPDATE Token t SET t.revoked = true WHERE t.user = :user AND t.revoked = false")
    void revokeAllByUser(@Param("user") User user);
}

