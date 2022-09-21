package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.UserInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.BooleanNode;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class JwtService {

    final JwtTokenProvider jwtTokenProvider;
    final UserInfoRepository userInfoRepository;


    public JwtService(
            JwtTokenProvider jwtTokenProvider,
            UserInfoRepository userInfoRepository
    ){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userInfoRepository = userInfoRepository;
    }

    // 이 메서드는 기존의 refreshToken이 존재하면 delete. +refresToken 삭제 이유 파악. userInfoController.login으로 메서드 호출함
    @Transactional
    public Boolean login(String email) {
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

    public Optional<UserInfo> getRefreshToken(String refreshToken){

        return userInfoRepository.findByRefreshToken(refreshToken);
    }

    public Map<String, String> validateRefreshToken(String refreshToken){
        UserInfo refreshToken1 = getRefreshToken(refreshToken).get();
        String createdAccessToken = jwtTokenProvider.validateRefreshToken(refreshToken1);

        return createRefreshJson(createdAccessToken);
    }

    public Map<String, String> createRefreshJson(String createdAccessToken){

        Map<String, String> map = new HashMap<>();

        if(createdAccessToken == null){

            map.put("errorType", "Forbidden");
            map.put("status", "401");
            map.put("message", "Refresh 토큰이 만료되었습니다. 로그인이 필요합니다.");

            return map;
        }
        //기존에 존재하는 accessToken 제거

        map.put("status", "200");
        map.put("message", "Refresh 토큰을 통한 Access Token 생성이 완료되었습니다.");
        map.put("accessToken", createdAccessToken);

        return map;

    }


}
