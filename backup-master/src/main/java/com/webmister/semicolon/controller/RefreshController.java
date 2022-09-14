package com.webmister.semicolon.controller;

import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class RefreshController {

    final UserInfoService userInfoService;
    final UserInfoRepository userInfoRepository;

    public RefreshController(
            UserInfoService userInfoService,
            UserInfoRepository userInfoRepository
    ){
        this.userInfoService = userInfoService;
        this.userInfoRepository = userInfoRepository;
    }

//    @RequestMapping(value = "/refresh",
//            method = {RequestMethod.GET, RequestMethod.POST}
//    )
//    public ResponseEntity<RefreshDto> validateRefreshToken(@RequestBody RefreshToken refreshToken){
//        HttpHeaders resHeaders = new HttpHeaders();
//        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
//
//        return new ResponseEntity<>(new RefreshDto(jwt),resHeaders, HttpStatus.OK);
//    }



}
