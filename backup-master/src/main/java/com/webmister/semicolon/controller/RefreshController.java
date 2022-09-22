package com.webmister.semicolon.controller;

import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.jwt.JwtFilter;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.RefreshApiResponseMessage;
import com.webmister.semicolon.request.UserInfoRequest;
import com.webmister.semicolon.service.JwtService;
import com.webmister.semicolon.service.UserInfoService;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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

//    @RequestMapping(value = "/refresh",
//            method = {RequestMethod.GET, RequestMethod.POST}
//    )
//    public ResponseEntity<RefreshApiResponseMessage> validateRefreshToken(@RequestBody HashMap<String, String> bodyJson){
//
//
//        Map<String, String> map = jwtService.validateRefreshToken(bodyJson.get("refreshToken"));
//
//        HttpHeaders resHeaders = new HttpHeaders();
//        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
//
//
//        if(map.get("status").equals("401")){
//            log.info("RefreshToken 만료");
//            RefreshApiResponseMessage refreshApiResponseMessage = new RefreshApiResponseMessage(map);
//            //HttpStatus.UNAUTHORIZED == 401 error
//            return new ResponseEntity<>(refreshApiResponseMessage, resHeaders, HttpStatus.UNAUTHORIZED);
//        }
//
//        log.info("RefreshToken 유효");
//        RefreshApiResponseMessage refreshApiResponseMessage = new RefreshApiResponseMessage(map);
//        return new ResponseEntity<>(refreshApiResponseMessage, resHeaders, HttpStatus.OK);
//
//    }

    @RequestMapping(value = "/refresh/{userNickname}",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<TokenDto> validateRefreshToken(@PathVariable("userNickname") String userNickname){

        log.info("/refresh/{userNickname}");
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        UserInfo userInfo = userInfoService.findUserInfoByUserNickname(userNickname);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userInfo.getUserEmail(), userInfo.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Boolean status = jwtTokenProvider.validateRefreshToken(userInfo.getRefreshToken());

        if(status == true) {
            TokenDto jwt = jwtTokenProvider.reCreateToken(authentication);

            resHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getAccessToken());

            return new ResponseEntity<>(jwt, resHeaders, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, resHeaders, HttpStatus.BAD_REQUEST);

    }
}
