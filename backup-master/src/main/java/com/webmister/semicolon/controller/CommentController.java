//package com.webmister.semicolon.controller;
//
//import com.webmister.semicolon.dto.CommentRequest;
//import com.webmister.semicolon.service.CommentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class CommentController {
//
//    @Autowired
//    CommentService commentService;
//
//    @PostMapping("")
//    public ResponseEntity<CommentRequest> createComment(@RequestBody CommentRequest commentRequest){
//        HttpHeaders resHeaders = new HttpHeaders();
//        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
//
//        try {
//            commentService.createComment(commentRequest);
//        }catch (Exception e){
//            return new ResponseEntity<>(Boolean.FALSE, resHeaders, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(Boolean.TRUE, resHeaders, HttpStatus.OK);
//    }
//
//    }
//
//}
