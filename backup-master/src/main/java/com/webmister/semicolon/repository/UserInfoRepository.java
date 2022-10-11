package com.webmister.semicolon.repository;

import com.webmister.semicolon.domain.UserInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByUserEmail(String email);
    boolean existsByUserNickname(String userNickname);

    boolean existsByPassword(String userPassword);

    Optional<UserInfo> findByUserEmailAndPassword(String email, String password);

    Optional<UserInfo> findByUserEmail(String email);

    UserInfo findUserInfoByUserNickname(String userNickname);

    Optional<UserInfo> findByPassword(String password);

    UserInfo findByUserNickname(String userNickname);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserInfo> findOneWithAuthoritiesByUserEmail(String userEmail);

    Optional<UserInfo> findByRefreshToken(String refreshToken);

    void deleteByUserEmail(String userEmail);

}