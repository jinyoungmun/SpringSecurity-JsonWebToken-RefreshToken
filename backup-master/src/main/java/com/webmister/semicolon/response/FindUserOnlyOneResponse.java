package com.webmister.semicolon.response;

import com.webmister.semicolon.domain.Report;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.EssentialReport;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FindUserOnlyOneResponse {
    String email;
    String password;
    List<EssentialReport> reportList;

    public FindUserOnlyOneResponse(UserInfo userInfo){
        this.reportList = new ArrayList<>();
        this.setEmail(userInfo.getUserEmail());
        this.setPassword(userInfo.getPassword());
        this.modifyReportToReport(userInfo.getReportList());
    }

    private void modifyReportToReport(List<Report> reportList){
        for(Report report : reportList){
            this.getReportList().add(new EssentialReport(report));
        }
    }

}
