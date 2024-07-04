package com.team20.backend.controller;

import com.team20.backend.model.Report;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.ForumService;
import com.team20.backend.service.ReportService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    @Mock
    private ReportService reportService;
    @Mock
    private ForumService forumService;
    @Mock
    private UsersService usersService;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private ReportController reportController;

    @Test
    public void reportTopic() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = reportController.reportTopic(token,1,"reason");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(usersService.findUserByUsername("name")).thenReturn(new Users());
        response = reportController.reportTopic(token,1,"reason");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void reportReply() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = reportController.reportReply(token,1,1,"res");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(usersService.findUserByUsername("name")).thenReturn(new Users());
        response = reportController.reportReply(token,1,1,"res");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getReports() {
        String token = "token";
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        ResponseEntity<List<Report>> response = reportController.getReports(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = reportController.getReports(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void deleteReport() {
        String token = "token";
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        ResponseEntity<String> response = reportController.deleteReport(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = reportController.deleteReport(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void handleReport() {
        String token = "token";
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        ResponseEntity<String> response = reportController.handleReport(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = reportController.handleReport(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Report report = new Report();
        report.setReplyId(0);
        Optional<Report> optionalReport = Optional.of(report);
        when(reportService.getReportById(1)).thenReturn(optionalReport);
        response = reportController.handleReport(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        report.setReplyId(1);
        response = reportController.handleReport(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
