package com.webmister.semicolon.controller;

import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.jwt.JwtFilter;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import com.webmister.semicolon.response.FindUserOnlyOneResponse;
import com.webmister.semicolon.request.Login;
import com.webmister.semicolon.request.UserInfoRequest;
import com.webmister.semicolon.request.FindUserOnlyOneRequest;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserInfoController(
            UserInfoService userInfoService,
            JwtTokenProvider jwtTokenProvider,
            JwtService jwtService,
            AuthenticationManagerBuilder authenticationManagerBuilder
    ) {
        this.userInfoService = userInfoService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtService = jwtService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @RequestMapping(value = "/printAll",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<List<UserInfo>> printAll(){
        List<UserInfo> userInfoList;

        userInfoList = userInfoService.findAll();

        System.out.println(userInfoList);

        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>(userInfoList ,resHeaders,  HttpStatus.OK);
    }

    @RequestMapping(value = "/get",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<FindUserOnlyOneResponse> createUser(@RequestBody FindUserOnlyOneRequest findUserOnlyOneRequest){
        UserInfo userInfo = userInfoService.findUserInfoById(findUserOnlyOneRequest.getId());

        FindUserOnlyOneResponse findUserOnlyOneResponse = new FindUserOnlyOneResponse(userInfo);

        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>( findUserOnlyOneResponse,resHeaders,  HttpStatus.OK);
    }

    @RequestMapping(value = "/login",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<TokenDto> login(@RequestBody Login login){
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        userInfoService.login(login);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getUserEmail(), login.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto jwt = jwtTokenProvider.createToken(authentication);

        jwtService.previousRefreshTokenDelete(login.getUserEmail());

        userInfoService.saveRefreshToken(login.getUserEmail(), jwt.getRefreshToken());
        // 기존 리프레시 지우고, 리프레시 유효시간 체크해서 남으면 자동 액세스 토큰 재발급 / 지났으면 401 에러 리턴하면서 클라이언트가 로그인.

        resHeaders.add(JwtFilter.AUTHORIZATION_HEADER, jwt.getAccessToken());
        return new ResponseEntity<>(jwt, resHeaders,  HttpStatus.OK);
    }


    @RequestMapping(value = "/{userNickName}",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<UserInfo> createUser(@PathVariable("userNickName") String userNickName){

        UserInfo user1 = userInfoService.findUserInfoByUserNickName(userNickName);
        log.debug(String.valueOf(user1));
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(user1 ,resHeaders,  HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Boolean> signUp(@RequestBody UserInfoRequest userInfoRequest) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        try {
            if (!userInfoService.checkDuplicateUserNickname(userInfoRequest.getUserNickName()))
                if(!userInfoService.checkDuplicateEmail(userInfoRequest.getUserEmail()))
                    userInfoService.signUp(userInfoRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(Boolean.FALSE, resHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Boolean.TRUE, resHeaders, HttpStatus.OK);
    }
}