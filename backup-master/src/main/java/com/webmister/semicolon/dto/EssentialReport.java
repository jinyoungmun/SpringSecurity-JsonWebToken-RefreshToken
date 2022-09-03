package com.webmister.semicolon.dto;

import com.webmister.semicolon.domain.Report;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EssentialReport {
    String title;
    String contents;
    String description;
    LocalDateTime reportCreateTime;
    LocalDateTime reportUpdateTime;

    public EssentialReport(Report report){
        this.title = report.getTitle();
        this.contents = report.getContents();
        this.reportCreateTime = report.getReportCreateTime();
        this.reportUpdateTime = report.getReportUpdateTime();
        this.description = null;
    }
}
