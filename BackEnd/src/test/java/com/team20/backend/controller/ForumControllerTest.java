package com.team20.backend.controller;


import com.team20.backend.dto.ForumReplyDTO;
import com.team20.backend.dto.TopicDTO;
import com.team20.backend.dto.TopicUserDTO;
import com.team20.backend.model.forum.ForumTopic;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.DTOService;
import com.team20.backend.service.ForumService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ForumControllerTest {
    @Mock
    private ForumService forumService;
    @Mock
    private UsersService userService;
    @Mock
    private DTOService dtoService;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private ForumController forumController;

    @Test
    public void createForumTopic() {
        String token = "token";
        ForumTopic forumTopic = new ForumTopic();
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = forumController.createForumTopic(token,forumTopic);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = forumController.createForumTopic(token,forumTopic);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void deleteForumTopic() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = forumController.deleteForumTopic(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = forumController.deleteForumTopic(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void addFavoriteTopic() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = forumController.addFavoriteTopic(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(1);
        when(userService.findUserByUsername("name")).thenReturn(users);
        List<ForumTopic> list = new ArrayList<>();
        ForumTopic topic = new ForumTopic();
        topic.setId(1);
        list.add(topic);
        when(forumService.getStarredTopics(users.getUserId())).thenReturn(list);
        response = forumController.addFavoriteTopic(token,1);
        assertThat(response.getBody()).isEqualTo("取消收藏成功");
        when(forumService.getStarredTopics(users.getUserId())).thenReturn(new ArrayList<>());
        response = forumController.addFavoriteTopic(token,1);
        assertThat(response.getBody()).isEqualTo("添加收藏成功");
    }
    @Test
    public void createForumReply() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = forumController.createForumReply(token,"content",1,2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(forumService.findForumTopicById(1)).thenReturn(new ForumTopic());
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(1);
        when(userService.findUserByUsername("name")).thenReturn(users);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = forumController.createForumReply(token,"content",1,2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void deleteForumReply() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = forumController.deleteForumReply(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = forumController.deleteForumReply(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void likeReply() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = forumController.likeReply(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(1);
        when(userService.findUserByUsername("name")).thenReturn(users);
        response = forumController.likeReply(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getForumTopic() {
        ResponseEntity<TopicDTO> response = forumController.getForumTopic(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getForumTopics() {
        ResponseEntity<List<TopicUserDTO>> response = forumController.getForumTopics();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testSearchForumTopics() {
        Date startDate = new Date();
        Date endDate = new Date();
        ResponseEntity<List<TopicUserDTO>> response = forumController.searchForumTopics("key",startDate,endDate);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getForumReplies() {
        ResponseEntity<List<ForumReplyDTO>> response = forumController.getForumReplies(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getSecondLevelReplies() {
        ResponseEntity<List<ForumReplyDTO>> response = forumController.getForumReplies(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getStarredTopics() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<TopicUserDTO>> response = forumController.getStarredTopics(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(1);
        when(userService.findUserByUsername("name")).thenReturn(users);
        response = forumController.getStarredTopics(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
