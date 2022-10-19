package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.Report;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.enumclass.DepartStatus;
import com.webmister.semicolon.repository.ReportRepository;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.DeleteReportRequest;
import com.webmister.semicolon.request.UploadRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class
ReportService {

    final ReportRepository reportRepository;
    final UserInfoRepository userInfoRepository;

    public ReportService(ReportRepository reportRepository, UserInfoRepository userInfoRepository) {
        this.reportRepository = reportRepository;
        this.userInfoRepository = userInfoRepository;
    }



    public Boolean reportUpload(UploadRequest uploadRequest, UserInfo userInfo, DepartStatus departStatus){
        try{
            reportRepository.save(Report.builder()
                    .title(uploadRequest.getTitle())
                    .contents(uploadRequest.getContents())
                    .likeCount(0)
                    .reportImageUrl(uploadRequest.getReportImageUrl())
                    .userDepartStatus(departStatus)
                    .userInfo(userInfo)
                    .build());
        }catch (Exception e){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Report findByReportId(Long id){
        return reportRepository.findById(id).orElse(new Report());
    }


    public Report findReportByUserIdAndReportId(Long userId, Long reportId){
        UserInfo userInfo = userInfoRepository.findById(userId).get();
        List<Report> reportList =  userInfo.getReportList();
        Report report1 = new Report();
        for (Report report : reportList){
            if(report.getReportId() == reportId){
                report1 = report;
                break;
            }
        }
        return report1;
    }

    public List<Report> findDepartAll(DepartStatus departStatus){

        return reportRepository.findAllByUserDepartStatus(departStatus).orElseThrow();
    }

    public Boolean deleteReport(DeleteReportRequest reportId, Long userId, DepartStatus departStatus){
        List<Report> reportList = userInfoRepository.findById(userId).get().getReportList();
        for(Report report : reportList) {
            if(report.getReportId() == reportId.getReportId() & report.getUserDepartStatus() == departStatus) {
                try{
                    reportRepository.deleteById(reportId.getReportId());
                }catch (Exception e){
                    return Boolean.FALSE;
                }
            }
        }

        return Boolean.TRUE;
    }
}
