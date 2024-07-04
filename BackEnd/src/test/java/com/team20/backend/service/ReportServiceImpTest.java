package com.team20.backend.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.team20.backend.model.Report;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class ReportServiceImpTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImp reportService;

    @Test
    public void reportTopic_ShouldCreateReport() {
        Users user = new Users();
        user.setUserId(1);
        int topicId = 10;
        String reason = "Inappropriate content";

        reportService.reportTopic(user, topicId, reason);

        then(reportRepository).should().save(any(Report.class));
    }
    @Test
    public void reportReply_ShouldCreateReport() {
        Users user = new Users();
        user.setUserId(1);
        int topicId = 10;
        int replyId = 5;
        String reason = "Spam";

        reportService.reportReply(user, topicId, replyId, reason);

        then(reportRepository).should().save(any(Report.class));
    }
    @Test
    public void getReports_ShouldReturnAllReports() {
        List<Report> expectedReports = Arrays.asList(new Report(), new Report());
        given(reportRepository.findAll()).willReturn(expectedReports);

        List<Report> reports = reportService.getReports();

        assertThat(reports).isEqualTo(expectedReports);
    }
    @Test
    public void deleteReport_ShouldDeleteReport() {
        int reportId = 1;

        reportService.deleteReport(reportId);

        then(reportRepository).should().deleteById(reportId);
    }
    @Test
    public void getReportById_ShouldReturnReport() {
        int reportId = 1;
        Report expectedReport = new Report();
        expectedReport.setId(reportId);
        given(reportRepository.findById(reportId)).willReturn(Optional.of(expectedReport));

        Optional<Report> report = reportService.getReportById(reportId);

        assertThat(report.get()).isEqualTo(expectedReport);
    }

}

