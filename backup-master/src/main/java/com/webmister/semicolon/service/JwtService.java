package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.Authority;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import com.webmister.semicolon.repository.AuthorityRepository;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.UserInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class JwtService {

    final JwtTokenProvider jwtTokenProvider;
    final UserInfoRepository userInfoRepository;
    final AuthorityRepository authorityRepository;
    final UserInfoService userInfoService;


    public JwtService(
            JwtTokenProvider jwtTokenProvider,
            UserInfoRepository userInfoRepository,
            AuthorityRepository authorityRepository,
            UserInfoService userInfoService
    ){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userInfoRepository = userInfoRepository;
        this.authorityRepository = authorityRepository;
        this.userInfoService = userInfoService;
    }

    @Transactional
    public Boolean previousRefreshTokenDelete(String email) {
        UserInfo userInfo = userInfoRepository.findByUserEmail(email).orElse(new UserInfo());

        try {
            if (userInfoRepository.existsByUserEmail(email) == true) {
                log.info("(구) refreshToken 삭제");
                userInfoRepository.save(userInfo.setRefreshToken(null));
                //userInfoRepository.deleteByUserEmail(email);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public String findRefreshToken(String email){
        UserInfo userInfo = userInfoRepository.findByUserEmail(email).orElse(new UserInfo());
        return userInfo.getRefreshToken();
    }

    public void getRole(String userNickName){
        UserInfo userInfo = userInfoService.findUserInfoByUserNickName(userNickName);
        //Authority authority = new Authority();
        Authority authority = userInfoService.findByAuthorityName("ROLE_USER");
        //userInfoRepository.save(userInfo.setAuthorities(null));
        authority.setAuthorityName("ROLE_USER");
        log.info(authority.getAuthorityName());
        userInfoRepository.save(userInfo.setAuthorities(Collections.singleton(authorityRepository.save(authority.setAuthorityName("ROLE_USER")))));
    }
    /*

    public Boolean authority(String email){

        UserInfo userInfo = userInfoRepository.findByUserEmail(email)
                .orElse(new UserInfo()); // 수정 필요한지 검토


        try {
            userInfoRepository.save(Collections.singleton(authorityRepository.save(Authority.setAuthorityName("ROLE_USER"))));

            log.info("리프레시 저장");
            log.info(userInfo.getRefreshToken());
            return Boolean.TRUE;

        }catch (Exception e){

            log.info("리프레시 실패");
            return Boolean.FALSE;
        }
    }

     */

    public Optional<UserInfo> getRefreshToken(String refreshToken){

        return userInfoRepository.findByRefreshToken(refreshToken);
    }
}
