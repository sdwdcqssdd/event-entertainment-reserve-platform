package com.team20.backend.controller;

import com.team20.backend.model.Report;
import com.team20.backend.service.ForumService;
import com.team20.backend.service.ReportService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ForumService forumService;

    @Autowired
    private UsersService usersService;
    @Resource
    private JwtUtils jwtUtils;
    /**
     * 举报话题
     * @param topicId 话题id
     * @param reason 举报原因
     * @return 举报结果
     */
    @PostMapping("/forum/topics/{topicId}/report")
    public ResponseEntity<String> reportTopic(@RequestHeader("Authorization") String token, @PathVariable int topicId, @RequestParam String reason) {
        // 保存举报信息到数据库
        if(!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        reportService.reportTopic(usersService.findUserByUsername(jwtUtils.getUsername(token)),topicId, reason);
        return ResponseEntity.ok("举报成功");
    }

    /**
     * 举报回复
     * @param topicId 话题id
     * @param replyId 回复id
     * @param reason 举报原因
     * @return 举报结果
     */
    @PostMapping("/forum/topics/{topicId}/replies/{replyId}/report")
    public ResponseEntity<String> reportReply(@RequestHeader("Authorization") String token, @PathVariable int topicId, @PathVariable int replyId, @RequestParam String reason) {
        // 保存举报信息到数据库
        if(!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        reportService.reportReply(usersService.findUserByUsername(jwtUtils.getUsername(token)),topicId, replyId, reason);
        return ResponseEntity.ok("举报成功");
    }

    /**
     * 获取所有举报信息
     * @return 所有举报信息
     */
    @GetMapping("/forum/reports")
    public ResponseEntity<List<Report>> getReports(@RequestHeader("Authorization") String token) {
        if(!jwtUtils.validateSuperUser(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(reportService.getReports());
    }

    /**
     * 超级用户删除举报信息
     * @param token token
     * @param reportId 举报信息id
     * @return 删除结果
     */
    @DeleteMapping("/forum/reports/{reportId}")
    public ResponseEntity<String> deleteReport(@RequestHeader("Authorization") String token, @PathVariable int reportId) {
        if(!jwtUtils.validateSuperUser(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        reportService.deleteReport(reportId);
        return ResponseEntity.ok("删除举报成功");
    }

    /**
     * 处理举报信息 如果是举报话题，删除话题 如果是举报回复，删除回复
     * @param reportId 举报信息id
     * @return 处理结果
     */
    @PostMapping("/forum/reports/{reportId}")
    public ResponseEntity<String> handleReport(@RequestHeader("Authorization") String token, @PathVariable int reportId) {
        if(!jwtUtils.validateSuperUser(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Report report = reportService.getReportById(reportId).orElse(null);
        if (report == null) {
            return ResponseEntity.badRequest().body("没找到举报信息");
        }
        if (report.getReplyId() == 0) {
            // 举报话题
            forumService.deleteForumTopicById(report.getTopicId());
        } else {
            // 举报回复
            forumService.deleteForumReplyById(report.getReplyId());
        }
        return ResponseEntity.ok("处理举报成功");
    }
}
