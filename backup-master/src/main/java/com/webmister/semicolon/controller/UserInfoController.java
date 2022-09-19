package com.webmister.semicolon.controller;

import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.jwt.JwtFilter;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.FindUserOnlyOneResponse;
import com.webmister.semicolon.request.Login;
import com.webmister.semicolon.request.TokenRequest;
import com.webmister.semicolon.request.UserInfoRequest;
import com.webmister.semicolon.response.FindUserOnlyOneRequest;
import com.webmister.semicolon.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Token;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoRepository userInfoRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserInfoController(
            UserInfoService userInfoService,
            JwtTokenProvider jwtTokenProvider,
            UserInfoRepository userInfoRepository,
            AuthenticationManagerBuilder authenticationManagerBuilder
    ) {
        this.userInfoService = userInfoService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userInfoRepository = userInfoRepository;
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
    public ResponseEntity<TokenDto> login(@RequestBody Login login, UserInfoRequest userInfoRequest){
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        //userInfoService.login(login)을 내리고. (내리는 이유는 jwtTokenProvider에서 토큰을 만들고 만든 토큰을 저장해야되기 때문에)
        //userInfoService 메서드 login에서 로그인을 하면 refreshTokenRepository.save로 refreshToken을 저장
        userInfoService.login(login);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getUserEmail(), login.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto jwt = jwtTokenProvider.createToken(authentication, userInfoRequest);


//        userInfoRepository.save(UserInfo.builder()
//                        .refreshToken(jwt.getRefreshToken()).build());
//        log.info(jwt.getRefreshToken());
        userInfoService.login1(jwt, userInfoRequest);

        //Bearer에 accessToken만 있어도 되는지 여부 확인. 사실상 refreshToken은 accessToken을 재발급 받기 위한 것이라
        resHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " +jwt.getAccessToken());
        return new ResponseEntity<>(jwt, resHeaders,  HttpStatus.OK);
    }


    @RequestMapping(value = "/{userNickname}",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<UserInfo> createUser(@PathVariable("userNickname") String userNickname){
        UserInfo user1 = userInfoService.findUserInfoByUserNickname(userNickname);
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
            if ( userInfoService.checkDuplicateUserNickname(userInfoRequest.getUserNickName()) == false)
                if(userInfoService.checkDuplicateEmail(userInfoRequest.getUserEmail()) == false)
                    userInfoService.signUp(userInfoRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(Boolean.FALSE, resHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Boolean.TRUE, resHeaders, HttpStatus.OK);
    }
}