package com.webmister.semicolon.controller;

import com.webmister.semicolon.service.MailService;
import com.webmister.semicolon.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class EmailController {
    final MailService mailService;
    final UserInfoService userInfoService;

    public EmailController(MailService mailService, UserInfoService userInfoService) {
        this.mailService = mailService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/signup/{userNickname}/{authKey}")
    public ResponseEntity<Boolean> checkingEmailAuth(@PathVariable("authKey") String userAuthKey,
                                                     @PathVariable("userNickname") String userNickname) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        try {
            userInfoService.mailAuthSuccess(userNickname);
        } catch (Exception e) {
            return new ResponseEntity<>(Boolean.FALSE, resHeaders, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Boolean.TRUE, resHeaders, HttpStatus.OK);
    }
}
