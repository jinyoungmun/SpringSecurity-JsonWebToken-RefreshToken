package com.webmister.semicolon.controller;

import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.RefreshApiResponseMessage;
import com.webmister.semicolon.service.JwtService;
import com.webmister.semicolon.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class RefreshController {

    final JwtService jwtService;
    final UserInfoRepository userInfoRepository;

    public RefreshController(
            JwtService jwtService,
            UserInfoRepository userInfoRepository
    ){
        this.jwtService = jwtService;
        this.userInfoRepository = userInfoRepository;
    }

    @RequestMapping(value = "/refresh",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<RefreshApiResponseMessage> validateRefreshToken(@RequestBody HashMap<String, String> bodyJson){


        Map<String, String> map = jwtService.validateRefreshToken(bodyJson.get("refreshToken"));

        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");

        if(map.get("status").equals("401")){
            log.info("RefreshToken 만료");
            RefreshApiResponseMessage refreshApiResponseMessage = new RefreshApiResponseMessage(map);
            //HttpStatus.UNAUTHORIZED == 401 error
            return new ResponseEntity<RefreshApiResponseMessage>(refreshApiResponseMessage, resHeaders, HttpStatus.UNAUTHORIZED);
        }

        log.info("RefreshToken 유효");
        RefreshApiResponseMessage refreshApiResponseMessage = new RefreshApiResponseMessage(map);
        return new ResponseEntity<RefreshApiResponseMessage>(refreshApiResponseMessage, resHeaders, HttpStatus.OK);

    }




}
