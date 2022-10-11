package com.webmister.semicolon.controller;

import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.jwt.JwtFilter;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.UserInfoRequest;
import com.webmister.semicolon.service.JwtService;
import com.webmister.semicolon.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class RefreshController {

    final JwtService jwtService;
    final UserInfoRepository userInfoRepository;
    final UserInfoService userInfoService;
    final JwtTokenProvider jwtTokenProvider;
    final AuthenticationManagerBuilder authenticationManagerBuilder;

    public RefreshController(
            JwtService jwtService,
            UserInfoRepository userInfoRepository,
            UserInfoService userInfoService,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder
    ){
        this.jwtService = jwtService;
        this.userInfoRepository = userInfoRepository;
        this.userInfoService = userInfoService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @RequestMapping(value = "/refresh/{userNickName}",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<TokenDto> validateRefreshToken(@PathVariable("userNickName") String userNickName){

        UserInfo userInfo = userInfoService.findUserInfoByUserNickname(userNickName);

        UserInfoRequest userInfoRequest = new UserInfoRequest();

        jwtService.requestSave(userInfoRequest, userInfo);
        jwtService.roleSetUp(userInfoRequest, userInfo);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userInfo.getUserEmail(), userInfo.getDecodedPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String refreshToken = jwtService.findRefreshToken(userInfo.getUserEmail());

        Boolean status = jwtTokenProvider.validateRefreshToken(refreshToken);

        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        if(status) {

            TokenDto jwt = jwtTokenProvider.reCreateToken(authentication);

            resHeaders.add(JwtFilter.AUTHORIZATION_HEADER, jwt.getAccessToken());

            return new ResponseEntity<>(jwt, resHeaders, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, resHeaders, HttpStatus.UNAUTHORIZED);

    }
}
