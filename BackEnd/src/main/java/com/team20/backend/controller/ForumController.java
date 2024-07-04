package com.team20.backend.controller;

import com.team20.backend.dto.*;
import com.team20.backend.model.forum.*;
import com.team20.backend.service.*;
import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ForumController {

    @Autowired
    private ForumService forumService;

    @Autowired
    private UsersService userService;
    @Autowired
    private DTOService dtoService;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * 创建论坛主题
     * @param token token
     * @param forumTopic 论坛主题
     * @return 创建结果
     */
    @PostMapping("/forum/topic")
    public ResponseEntity<String> createForumTopic(@RequestHeader("Authorization") String token, @RequestBody ForumTopic forumTopic) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        forumTopic.setStar(0);
        forumTopic.setReplyNum(0);
        forumService.saveForumTopic(forumTopic);
        return ResponseEntity.ok("话题创建成功");
    }

    /**
     * 删除论坛主题
     * @param token token
     * @param topicId 主题id
     * @return 删除结果
     */
    @DeleteMapping("/forum/topic/delete/{topicId}")
    public ResponseEntity<String> deleteForumTopic(@RequestHeader("Authorization") String token, @PathVariable("topicId") int topicId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        forumService.deleteForumTopicById(topicId);

        return ResponseEntity.ok("话题删除成功");
    }

    /**
     * 收藏或取消收藏主题
     * @param token token
     * @param topicId 主题id
     * @return 收藏结果
     */
    @PostMapping("/forum/topic/{topicId}/star")
    @Transactional
    public ResponseEntity<String> addFavoriteTopic(@RequestHeader("Authorization") String token, @PathVariable("topicId") int topicId) {
        // 验证用户身份
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 获取当前用户信息
        int userId = userService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        // 判断是否已经收藏
        if (forumService.getStarredTopics(userId).stream().anyMatch(topic -> topic.getId() == topicId)) {
            // 已经收藏 取消收藏
            forumService.deleteStar(userId, topicId);
            return ResponseEntity.ok("取消收藏成功");
        }
        // 添加收藏关系
        forumService.addStar(userId, topicId);

        return ResponseEntity.ok("添加收藏成功");
    }

    /**
     * 创建论坛回复
     * @param token token
     * @param content 内容
     * @param topicId 主题id
     * @return 回复结果
     */
    @PostMapping("/forum/reply")
    public ResponseEntity<String> createForumReply(@RequestHeader("Authorization") String token,
                                                   @RequestParam("content") String content,
                                                   @RequestParam("topicId") int topicId,
                                                   @RequestParam("parentId") int parentId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ForumReply forumReply = new ForumReply();
        forumReply.setPostTime(new Date());
        forumReply.setContent(content);
        forumReply.setForumTopic(forumService.findForumTopicById(topicId));
        forumReply.setLikeNum(0);
        forumReply.setUserId(userService.findUserByUsername(jwtUtils.getUsername(token)).getUserId());
        forumReply.setParentId(parentId);
        forumService.saveForumReply(forumReply);
        return ResponseEntity.ok("回复成功");
    }
    /**
     * 删除论坛回复
     * @param token token
     * @param replyId 回复id
     * @return 删除结果
     */
    @DeleteMapping("/forum/reply/delete/{replyId}")
    public ResponseEntity<String> deleteForumReply(@RequestHeader("Authorization") String token, @PathVariable("replyId") int replyId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        forumService.deleteForumReplyById(replyId);
        return ResponseEntity.ok("删除成功");
    }

    /**
     * 点赞回复或取消点赞
     * @param token token
     * @param replyId 回复id
     * @return 点赞结果
     */
    @PostMapping("/forum/reply/like/{replyId}")
    @Transactional
    public ResponseEntity<String> likeReply(@RequestHeader("Authorization") String token, @PathVariable("replyId") int replyId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int userId = userService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        forumService.likeReply(userId, replyId);
        return ResponseEntity.ok("操作成功");
    }

    /**
     * 获取论坛主题
     * @param topicId 主题id
     * @return 论坛主题
     */
    @GetMapping("/forum/{topicId}")
    public ResponseEntity<TopicDTO> getForumTopic(@PathVariable("topicId") int topicId) {
        return ResponseEntity.ok().body(dtoService.getTopicDTO(topicId));
    }

    /**
     * 获取论坛主题列表
     * @return 论坛主题列表
     */
    @GetMapping("/forum")
    public ResponseEntity<List<TopicUserDTO>> getForumTopics() {
        return ResponseEntity.ok().body(forumService.getAllForumTopics().stream().map(topic -> {
//            if( == null) {
//                return null;
//            }
            UserDTO user = dtoService.getUserDTO(topic.getUserId());

            return new TopicUserDTO(topic, user);
        }).toList());
    }

    /**
     * 根据关键字和时间范围搜索论坛主题
     * @param keyword 搜索关键字
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 符合条件的论坛主题列表
     */
    @GetMapping("/forum/search")
    public ResponseEntity<List<TopicUserDTO>> searchForumTopics(
            @RequestParam String keyword,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {

        List<ForumTopic> forumTopics = forumService.searchForumTopics(keyword, startDate, endDate);
        // 将 ForumTopic 转换为 TopicUserDTO 并返回
        List<TopicUserDTO> topicUserDTOs = forumTopics.stream().map(topic -> {
            UserDTO user = dtoService.getUserDTO(topic.getUserId());
            return new TopicUserDTO(topic, user);
        }).toList();

        return ResponseEntity.ok(topicUserDTOs);
    }


    /**
     * 获取论坛回复
     * @param topicId 主题id
     * @return 论坛回复列表
     */
    @GetMapping("/forum/{topicId}/replies")
    public ResponseEntity<List<ForumReplyDTO>> getForumReplies(@PathVariable("topicId") int topicId) {
        return ResponseEntity.ok().body(dtoService.getForumReplies(topicId).stream().peek(reply ->
                        reply.setHasSecondaryReplies(forumService.hasSecondaryReplies(reply.getReply().getId())))
                .collect(Collectors.toList()));
    }
    /**
     * 获取二级回复
     * @param replyId 回复id
     * @return 二级回复列表
     */
    @GetMapping("/forum/reply/{replyId}/replies")
    public ResponseEntity<List<ForumReplyDTO>> getSecondLevelReplies(@PathVariable("replyId") int replyId) {
        return ResponseEntity.ok().body(dtoService.getForumRepliesByReplyId(replyId));
    }
    /**
     * 获取用户收藏的主题
     * @param token token
     * @return 用户收藏的主题列表
     */
    @GetMapping("/forum/starred")
    public ResponseEntity<List<TopicUserDTO>> getStarredTopics(@RequestHeader("Authorization") String token) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int userId = userService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        return ResponseEntity.ok().body(forumService.getStarredTopics(userId).stream().map(topic -> {
            UserDTO user = dtoService.getUserDTO(topic.getUserId());
            return new TopicUserDTO(topic, user);
        }).toList());
    }
}

