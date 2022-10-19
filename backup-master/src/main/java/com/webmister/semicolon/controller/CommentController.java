package com.webmister.semicolon.controller;

import com.webmister.semicolon.request.CommentRequest;
import com.webmister.semicolon.service.CommentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommentController {

    final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/commentUpload")
    public ResponseEntity<Boolean> createComment(@RequestBody CommentRequest commentRequest){
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        try {
            commentService.createComment(commentRequest);
        }catch (Exception e){
            return new ResponseEntity<>(Boolean.FALSE, resHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Boolean.TRUE, resHeaders, HttpStatus.OK);
    }

}
