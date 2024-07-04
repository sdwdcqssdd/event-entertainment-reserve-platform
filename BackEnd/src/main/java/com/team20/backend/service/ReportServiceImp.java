package com.team20.backend.service;

import com.team20.backend.model.Report;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImp implements ReportService{
    @Autowired
    private ReportRepository reportRepository;
    @Override
    public void reportTopic(Users user, int topicId, String reason) {
        Report report = new Report();
        report.setReplyId(0);
        report.setTopicId(topicId);
        report.setReason(reason);
        reportRepository.save(report);
    }
    @Override
    public void reportReply(Users user, int topicId, int replyId, String reason) {
        Report report = new Report();
        report.setTopicId(topicId);
        report.setReplyId(replyId);
        report.setReason(reason);
        reportRepository.save(report);
    }
    @Override
    public List<Report> getReports() {
        return reportRepository.findAll();
    }
    @Override
    public void deleteReport(int reportId) {
        reportRepository.deleteById(reportId);
    }
    @Override
    public Optional<Report> getReportById(int reportId) {
        return reportRepository.findById(reportId);
    }
}
