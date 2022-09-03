package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.Comment;
import com.webmister.semicolon.domain.Report;
import com.webmister.semicolon.dto.CommentRequest;
import com.webmister.semicolon.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public Boolean createComment(CommentRequest commentRequest)  {
        try{
            commentRepository.save(Comment.builder()
                    .id(commentRequest.getId())
                    .comment(commentRequest.getComment())
                    .userInfo(commentRequest.getUserInfo())
                    .report(commentRequest.getReport())
                    .build());
            return Boolean.TRUE;
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }

}
