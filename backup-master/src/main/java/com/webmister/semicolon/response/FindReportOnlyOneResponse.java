package com.webmister.semicolon.response;

import com.webmister.semicolon.domain.Comment;
import com.webmister.semicolon.domain.Report;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FindReportOnlyOneResponse {
    String title;
    String contents;
    List<Comment> commentList;

    public FindReportOnlyOneResponse(Report report){
        this.setTitle(report.getTitle());
        this.setContents(report.getContents());
        this.commentList = new ArrayList<>();
    }

}
