package com.webmister.semicolon.repository;

import com.webmister.semicolon.domain.UserInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<UserInfo, Long> {

//    Optional<UserInfo> findByRefreshToken(String refreshToken);
//
//
//    boolean existsByRefreshToken(String refreshToken);
//
//    void deleteByRefreshToken(String refreshToken);
}
