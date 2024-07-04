package com.team20.backend.controller;


import com.team20.backend.dto.EventDTO;
import com.team20.backend.dto.EventQuery;
import com.team20.backend.dto.UserEventDTO;
import com.team20.backend.model.Event;
import com.team20.backend.model.UserEvent;
import com.team20.backend.model.Venue;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.*;
import com.team20.backend.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.text.ParseException;
import java.util.*;


@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private UsersService usersService;

    @Mock
    private EventService eventService;

    @Mock
    private VenueService venueService;

    @Mock
    private CommentService commentService;

    @Mock
    private UserEventService userEventService;

    @Mock
    private DTOService dtoService;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private EventController eventController;
    @Test
    public void getRecommendEventWithInvalidToken() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<EventDTO>> response = eventController.getRecommendEvent(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    public void getRecommendEventWithValidToken() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(true);
        String username = "username";
        when(jwtUtils.getUsername(token)).thenReturn(username);
        Users users = new Users();
        users.setUserId(1);
        when(usersService.findUserByUsername(username)).thenReturn(users);

        List<EventDTO> eventDTOS = new ArrayList<>();
        when(eventService.findEventsAfterCurrentDate().stream().map(dtoService::convertToDTO).toList()).thenReturn(eventDTOS);
        ResponseEntity<List<EventDTO>> response = eventController.getRecommendEvent(token);
        assertThat(response.getBody()).isEqualTo(eventDTOS);
    }
    @Test
    public void getRecommendEventWithValidTokenAndEvent() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(true);
        String username = "username";
        when(jwtUtils.getUsername(token)).thenReturn(username);
        Users users = new Users();
        users.setUserId(1);
        when(usersService.findUserByUsername(username)).thenReturn(users);
        List<UserEvent> appoints = new ArrayList<>();
        UserEvent userEvent = new UserEvent();
        userEvent.setEventId(1);
        Event event = new Event();
        event.setEventId(1);
        event.setCategoryId(2);
        event.setVenueId(3);
        when(eventService.findByEventId(1)).thenReturn(event);
        appoints.add(userEvent);
        when(userEventService.findUserEventByUserId(1)).thenReturn(appoints);
        List<Event> recommend = new ArrayList<>();
        when(eventService.recommend(any(),any())).thenReturn(recommend);
        ResponseEntity<List<EventDTO>> response = eventController.getRecommendEvent(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getEvents() {
        Users user = new Users();
        user.setUserId(1);
        when(usersService.findUserByUsername("name")).thenReturn(user);
        Venue venue = new Venue();
        when(venueService.findByName("v")).thenReturn(venue);
        EventQuery eventQuery = new EventQuery();
        List<String> venues = new ArrayList<>();
        List<String> organizerName = new ArrayList<>();
        organizerName.add("name");
        venues.add("v");
        eventQuery.setVenueNames(venues);
        eventQuery.setOrganizerNames(organizerName);
        ResponseEntity<List<EventDTO>> response = eventController.getEvents(eventQuery);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getEventById() {
        Event event = new Event();
        event.setEventId(1);
        when(eventService.findByEventId(1)).thenReturn(event);
        ResponseEntity<EventDTO> response = eventController.getEventById(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        response = eventController.getEventById(0);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void getEventsAfterCurrentDate() {
        ResponseEntity<List<EventDTO>> response = eventController.getEventsAfterCurrentDate();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getPendingEvents() {
        String token = "invalid";
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        ResponseEntity<List<Event>> response = eventController.getPendingEvents(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        token = "valid";
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = eventController.getPendingEvents(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void approveEvent() {
        String token = "invalid";
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        ResponseEntity<String> response = eventController.approveEvent(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        token = "valid";
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = eventController.approveEvent(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void rejectEvent() {
        String token = "invalid";
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        ResponseEntity<String> response = eventController.rejectEvent(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        token = "valid";
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = eventController.rejectEvent(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getAppoint() {
        String token = "invalid";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<UserEvent>> response = eventController.getAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        token = "valid";
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = eventController.getAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        String username = "name";
        when(jwtUtils.getUsername(token)).thenReturn(username);
        Users users = new Users();
        users.setUserId(1);
        when(usersService.findUserByUsername(username)).thenReturn(users);
        Event event = new Event();
        event.setEventId(1);
        event.setOrganizerId(1);
        when(eventService.findByEventId(1)).thenReturn(event);
        response = eventController.getAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        event.setOrganizerId(2);
        response = eventController.getAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    public void approveAppoint() {
        String token = "invalid";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = eventController.approveAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        token = "valid";
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = eventController.approveAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        String username = "name";
        when(jwtUtils.getUsername(token)).thenReturn(username);
        Users users = new Users();
        users.setUserId(1);
        when(usersService.findUserByUsername(username)).thenReturn(users);
        when(userEventService.getEventOrganizerByUserEventId(1)).thenReturn(1);
        response = eventController.approveAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        when(userEventService.getEventOrganizerByUserEventId(1)).thenReturn(2);
        response = eventController.approveAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    public void rejectAppoint() {
        String token = "invalid";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = eventController.rejectAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        token = "valid";
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.validateSuperUser(token)).thenReturn(true);
        response = eventController.rejectAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        when(jwtUtils.validateSuperUser(token)).thenReturn(false);
        String username = "name";
        when(jwtUtils.getUsername(token)).thenReturn(username);
        Users users = new Users();
        users.setUserId(1);
        when(usersService.findUserByUsername(username)).thenReturn(users);
        when(userEventService.getEventOrganizerByUserEventId(1)).thenReturn(1);
        response = eventController.rejectAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        when(userEventService.getEventOrganizerByUserEventId(1)).thenReturn(2);
        response = eventController.rejectAppoint(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    public void getEventsOnDate() throws ParseException {
        String dataString = "2024-6-3";
        ResponseEntity<List<EventDTO>> response = eventController.getEventsOnDate(dataString);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void createEvent() {
        String token = "token";
        Event event = new Event();
        event.setVenueId(1);
        Users users = new Users();
        users.setUserId(1);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(usersService.findUserByUsername("name")).thenReturn(users);
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<Map<String,String>> response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        event.setTitle("title");
        response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        event.setDate(new Date());
        response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        event.setStartTime(new Date(1123));
        response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        event.setEndTime(new Date(1980));
        response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Venue venue = new Venue();
        when(venueService.findByVenueId(1)).thenReturn(venue);
        response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        event.setCapacityLimit(100);
        venue.setCapacity(50);
        response = eventController.createEvent(token,event);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    public void appoint() {
        String token = "token";
        Users users = new Users();
        users.setUserId(1);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(usersService.findUserByUsername("name")).thenReturn(users);
        ResponseEntity<Map<String,String>> response = eventController.appoint(token,1);
        assertThat(response.getBody().get("message")).isEqualTo("预约成功");
    }
    @Test
    public void getUserHistoryEvents() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<UserEventDTO>> response = eventController.getUserHistoryEvents(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(1);
        when(usersService.findUserByUsername("name")).thenReturn(users);
        response = eventController.getUserHistoryEvents(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}