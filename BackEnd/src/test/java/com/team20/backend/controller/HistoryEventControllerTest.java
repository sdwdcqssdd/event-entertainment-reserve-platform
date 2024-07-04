package com.team20.backend.controller;

import com.team20.backend.dto.EventCommentDTO;
import com.team20.backend.dto.EventDTO;
import com.team20.backend.dto.EventQuery;
import com.team20.backend.model.Venue;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.*;
import com.team20.backend.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoryEventControllerTest {
    @Mock
    private UsersService usersService;
    @Mock
    private EventService eventService;
    @Mock
    private CommentService commentService;
    @Mock
    private VenueService venueService;
    @Mock
    private AvatarService avatarService;
    @Mock
    private DTOService dtoService;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private HistoryEventController historyEventController;
    @Test
    public void getHistoryEventById() {
        ResponseEntity<EventCommentDTO> response = historyEventController.getHistoryEventById(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getHistoryEvents() {
        EventQuery eventQuery = new EventQuery();
        List<String> organizerNames = new ArrayList<>();
        organizerNames.add("name");
        eventQuery.setOrganizerNames(organizerNames);
        Users users = new Users();
        users.setUserId(1);
        when(usersService.findUserByUsername("name")).thenReturn(users);
        List<String> venueNames = new ArrayList<>();
        venueNames.add("venueName");
        eventQuery.setVenueNames(venueNames);
        Venue venue = new Venue();
        venue.setVenueId(1);
        when(venueService.findByName("venueName")).thenReturn(venue);
        ResponseEntity<List<EventDTO>> response = historyEventController.getHistoryEvents(eventQuery);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void addComment() {
        doThrow(new IllegalArgumentException("Rating must be between 0 and 10")).when(commentService).addComment(2,3,"content",15);
        doThrow(new IllegalStateException("User has already commented on this event")).when(commentService).addComment(1,2,"content",9);
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<?> response = historyEventController.addComment(token,1,"content",9);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        when(usersService.findUserByUsername("name")).thenReturn(users);
        users.setUserId(2);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = historyEventController.addComment(token,3,"content",15);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        users.setUserId(1);
        response = historyEventController.addComment(token,2,"content",9);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        response = historyEventController.addComment(token,6,"content",9);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void modifyComment() {
        doThrow(new IllegalStateException("User has not commented on this event")).when(commentService).modifyComment(1,1,"content",10);
        doThrow(new IllegalArgumentException("User can only modify their own comments")).when(commentService).modifyComment(1,2,"content",10);
        doThrow(new IllegalArgumentException("Rating must be between 0 and 10")).when(commentService).modifyComment(1,3,"content",11);
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<?> response = historyEventController.modifyComment(token,1,"content",9);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(jwtUtils.validateToken(token)).thenReturn(true);
        Users users = new Users();
        when(usersService.findUserByUsername("name")).thenReturn(users);
        users.setUserId(1);
        response = historyEventController.modifyComment(token,1,"content",10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        response = historyEventController.modifyComment(token,2,"content",10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        response = historyEventController.modifyComment(token,3,"content",11);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        response = historyEventController.modifyComment(token,3,"content",10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void deleteComment() {
        doThrow(new IllegalStateException("User has not commented on this event")).when(commentService).deleteComment(1,1);
        doThrow(new IllegalArgumentException("User can only delete their own comments")).when(commentService).deleteComment(1,2);
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<?> response = historyEventController.deleteComment(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(jwtUtils.validateToken(token)).thenReturn(true);
        Users users = new Users();
        when(usersService.findUserByUsername("name")).thenReturn(users);
        users.setUserId(1);
        response = historyEventController.deleteComment(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        response = historyEventController.deleteComment(token,2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        response = historyEventController.deleteComment(token,3);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
