package com.team20.backend.service;

import com.team20.backend.model.Report;
import com.team20.backend.model.user.Users;

import java.util.List;
import java.util.Optional;

public interface ReportService {
    void reportTopic(Users user, int topicId, String reason);

    void reportReply(Users user, int topicId, int replyId, String reason);

    List<Report> getReports();

    void deleteReport(int reportId);

    Optional<Report> getReportById(int reportId);
}
