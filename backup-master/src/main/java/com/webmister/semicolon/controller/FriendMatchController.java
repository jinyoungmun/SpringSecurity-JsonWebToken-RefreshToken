package com.webmister.semicolon.controller;

import com.webmister.semicolon.domain.FriendMatch;
import com.webmister.semicolon.request.FriendMatchRequest;
import com.webmister.semicolon.response.FriendMatchResponse;
import com.webmister.semicolon.service.FriendMatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class FriendMatchController {
    final FriendMatchService friendMatchService;

    public FriendMatchController(FriendMatchService friendMatchService){
        this.friendMatchService = friendMatchService;
    }

    @PostMapping("/{postFriendNickname}/friendMatch")
    public ResponseEntity<FriendMatchResponse> friendMatch(@PathVariable("postFriendNickname") String postFriendNickname,
                                                           @RequestBody FriendMatchRequest friendMatchRequest) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        FriendMatchResponse friendMatchResponse = new FriendMatchResponse();
        try {
            friendMatchResponse = friendMatchService.FriendMatchSave(postFriendNickname, friendMatchRequest);
        }catch (Exception e){
            return new ResponseEntity<>(new FriendMatchResponse(), resHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(friendMatchResponse, resHeaders, HttpStatus.OK);
    }

    @PostMapping("/{userNickname}/friendPrintAll")
    public ResponseEntity<List<FriendMatch>> friendList(@PathVariable("userNickname") String userNickname){
        List<FriendMatch> friendMatchList;
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        friendMatchList = friendMatchService.FriendList(userNickname);
        log.debug(String.valueOf(friendMatchList));
        return new ResponseEntity<>(friendMatchList,resHeaders,HttpStatus.OK);
    }



    @DeleteMapping("/{departStatus}/{postFriendNickname}/friendDelete")
    public ResponseEntity<Boolean> friendDelete(@PathVariable("departStatus") String departStatus,
                                                @PathVariable("postFriendNickname") String postFriendNickname,
                                                @RequestBody FriendMatchRequest friendMatchRequest) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        try {
            friendMatchService.FriendMatchDelete(postFriendNickname, friendMatchRequest);
        }catch (Exception e){
            return new ResponseEntity<>(Boolean.FALSE, resHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(Boolean.TRUE, resHeaders, HttpStatus.OK);
    }

}
