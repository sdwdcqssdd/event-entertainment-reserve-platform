package com.team20.backend.service;

import com.team20.backend.model.history.Comment;
import com.team20.backend.model.Event;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class CommentServiceImpTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private CommentServiceImp commentService;

    private Users user;
    private Event event;
    private Comment comment;

    @BeforeEach
    public void setup() {
        user = new Users();  // Assume Users has a default constructor
        user.setUserId(1);   // Setup user id
        event = new Event(); // Assume Event has a default constructor
        event.setEventId(1); // Setup event id


        when(eventService.findByEventId(1)).thenReturn(event);
    }

    @Test
    public void addComment_ValidInput_ReturnsComment() {
        // 设置特定于此测试方法的模拟
        when(usersService.findUserById(1)).thenReturn(user);
        when(eventService.findByEventId(1)).thenReturn(event);
        when(commentRepository.findByUserAndEvent(user, event)).thenReturn(Optional.empty());

        Comment newComment = new Comment();
        newComment.setContent("Great event!");
        newComment.setRating(8);
        newComment.setUser(user);
        newComment.setEvent(event);
        newComment.setCreated_at(new Date());

        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);

        Comment result = commentService.addComment(1, 1, "Great event!", 8);
        assertThat(result.getContent()).isEqualTo("Great event!");
        assertThat(result.getRating()).isEqualTo(8);

        verify(usersService).findUserById(1);  // 确认此模拟被调用
    }


    @Test
    public void modifyComment_ValidInput_ReturnsUpdatedComment() {
        // 设置特定于此测试方法的模拟
        when(usersService.findUserById(1)).thenReturn(user);
        when(eventService.findByEventId(1)).thenReturn(event);
        Comment existingComment = new Comment();
        existingComment.setId(1);
        existingComment.setContent("Old Comment");
        existingComment.setRating(5);
        existingComment.setUser(user);
        existingComment.setEvent(event);
        when(commentRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        Comment result = commentService.modifyComment(1, 1, "Updated Comment", 7);
        assertThat(result.getContent()).isEqualTo("Updated Comment");
        assertThat(result.getRating()).isEqualTo(7);
    }


    @Test
    public void deleteComment_ValidInput_PerformsDeletion() {
        // 设置特定于此测试方法的模拟
        when(usersService.findUserById(1)).thenReturn(user);
        when(eventService.findByEventId(1)).thenReturn(event);
        Comment existingComment = new Comment();
        existingComment.setId(1);
        existingComment.setUser(user);
        existingComment.setEvent(event);
        when(commentRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(existingComment));
        doNothing().when(commentRepository).delete(existingComment);

        commentService.deleteComment(1, 1);
        verify(commentRepository).delete(existingComment);
    }


    @Test
    public void findCommentsByEventId_ReturnsComments() {
        // 设置特定于此测试方法的模拟
        when(eventService.findByEventId(1)).thenReturn(event);
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());
        when(commentRepository.getCommentsByEvent(event)).thenReturn(comments);

        List<Comment> results = commentService.findCommentsByEventId(1);
        assertThat(results).hasSize(2);
    }

    // Testing addComment with invalid rating
    @Test
    public void addComment_InvalidRating_ShouldThrowException() {
        int userId = 1;
        int eventId = 1;
        String content = "Nice event";
        int invalidRating = 11;

        Users user = new Users();
        Event event = new Event();
        given(usersService.findUserById(userId)).willReturn(user);
        given(eventService.findByEventId(eventId)).willReturn(event);

        assertThatThrownBy(() -> commentService.addComment(userId, eventId, content, invalidRating))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 10");
    }

    // Testing modifyComment when user has not previously commented
    @Test
    public void modifyComment_NoExistingComment_ShouldThrowException() {
        int userId = 1;
        int eventId = 1;
        String content = "Updated Comment";
        int rating = 5;

        Users user = new Users();
        Event event = new Event();
        given(usersService.findUserById(userId)).willReturn(user);
        given(eventService.findByEventId(eventId)).willReturn(event);
        given(commentRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.modifyComment(userId, eventId, content, rating))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User has not commented on this event");
    }

    // Testing deleteComment when user tries to delete a comment they did not make
    @Test
    public void deleteComment_NoExistingComment_ShouldThrowException() {
        int userId = 1;
        int eventId = 1;

        Users user = new Users();
        Event event = new Event();
        given(usersService.findUserById(userId)).willReturn(user);
        given(eventService.findByEventId(eventId)).willReturn(event);
        given(commentRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(userId, eventId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User has not commented on this event");
    }
    // Testing successful addComment
    @Test
    public void addComment_Successful_ShouldReturnComment() {
        int userId = 1;
        int eventId = 1;
        String content = "Great event!";
        int rating = 8;

        Users user = new Users();
        Event event = new Event();
        given(usersService.findUserById(userId)).willReturn(user);
        given(eventService.findByEventId(eventId)).willReturn(event);
        given(commentRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setEvent(event);
        comment.setContent(content);
        comment.setRating(rating);
        comment.setCreated_at(new Date());

        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        Comment result = commentService.addComment(userId, eventId, content, rating);
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(rating, result.getRating());
    }

    // Testing successful modifyComment
    @Test
    public void modifyComment_Successful_ShouldReturnUpdatedComment() {
        int userId = 1;
        int eventId = 1;
        String content = "Updated content";
        int rating = 9;

        Users user = new Users();
        user.setUserId(userId);
        Event event = new Event();
        Comment existingComment = new Comment();
        existingComment.setUser(user);
        existingComment.setEvent(event);

        given(usersService.findUserById(userId)).willReturn(user);
        given(eventService.findByEventId(eventId)).willReturn(event);
        given(commentRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(existingComment));

        Comment updatedComment = new Comment();
        updatedComment.setUser(user);
        updatedComment.setEvent(event);
        updatedComment.setContent(content);
        updatedComment.setRating(rating);
        updatedComment.setCreated_at(new Date());

        given(commentRepository.save(any(Comment.class))).willReturn(updatedComment);

        Comment result = commentService.modifyComment(userId, eventId, content, rating);
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(rating, result.getRating());
    }

    // Testing successful deleteComment
    @Test
    public void deleteComment_Successful_ShouldCompleteWithoutError() {
        int userId = 1;
        int eventId = 1;

        Users user = new Users();
        user.setUserId(userId);
        Event event = new Event();
        Comment existingComment = new Comment();
        existingComment.setUser(user);

        given(usersService.findUserById(userId)).willReturn(user);
        given(eventService.findByEventId(eventId)).willReturn(event);
        given(commentRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(existingComment));

        commentService.deleteComment(userId, eventId);
        then(commentRepository).should().delete(existingComment);
    }



}
