package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.Comment;
import com.webmister.semicolon.domain.Report;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.repository.CommentRepository;
import com.webmister.semicolon.repository.ReportRepository;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.CommentRequest;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    final CommentRepository commentRepository;
    final ReportRepository reportRepository;
    final UserInfoRepository userInfoRepository;

    public CommentService(CommentRepository commentRepository, ReportRepository reportRepository,
                          UserInfoRepository userInfoRepository) {
        this.commentRepository = commentRepository;
        this.reportRepository = reportRepository;
        this.userInfoRepository = userInfoRepository;
    }


    public Boolean createComment(CommentRequest commentRequest)  {
        try{
            Report report = reportRepository.findById(commentRequest.getReportId()).get();
            UserInfo userInfo = userInfoRepository.findById(commentRequest.getUserId()).get();
            commentRepository.save(Comment.builder()
                    .comment(commentRequest.getComment())
                    .userInfo(userInfo)
                    .report(report)
                    .build());
        }catch (Exception e){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Comment findCommentById(Long id){
        return commentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("존재하지 않는 댓글입니다."));
    }

}
