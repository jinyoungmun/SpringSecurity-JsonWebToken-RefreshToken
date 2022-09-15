package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.RefreshToken;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import com.webmister.semicolon.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class JwtService {

    final JwtTokenProvider jwtTokenProvider;
    final RefreshTokenRepository refreshTokenRepository;


    public JwtService(
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenRepository refreshTokenRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

//    @Transactional
//    public void login(TokenDto tokenDto){
//        RefreshToken refreshToken = RefreshToken.builder()..refreshToken(tokenDto.getRefreshToken()).build();
//        if(refreshTokenRepository.existsById(id)){
//            // .RefreshToken.exists()
//        }
//    }

    public Optional<RefreshToken> getRefreshToken(String refreshToken){

        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public Map<String, String> validateRefreshToken(String refreshToken){
        RefreshToken refreshToken1 = getRefreshToken(refreshToken).get();
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

        map.put("status", "200");
        map.put("message", "Refresh 토큰을 통한 Access Token 생성이 완료되었습니다.");
        map.put("accessToken", createdAccessToken);

        return map;

    }


}
