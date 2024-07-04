package com.team20.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import com.team20.backend.model.UserEvent;
import com.team20.backend.model.Event;
import com.team20.backend.repository.UserEventRepository;
import com.team20.backend.repository.EventRepository;
import com.team20.backend.service.UserEventServiceImp;
import com.team20.backend.service.EmailService;

import java.util.List;
import java.util.Optional;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class UserEventServiceImpTest {

    @Mock
    private UserEventRepository userEventRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserEventServiceImp userEventService;

    @Test
    public void reserveEvent_ShouldSaveUserEventWithPendingStatus() {
        int userId = 1;
        int eventId = 101;

        userEventService.reserveEvent(userId, eventId);

        ArgumentCaptor<UserEvent> captor = ArgumentCaptor.forClass(UserEvent.class);
        then(userEventRepository).should().save(captor.capture());
        UserEvent captured = captor.getValue();

        assertThat(captured.getUserId()).isEqualTo(userId);
        assertThat(captured.getEventId()).isEqualTo(eventId);
        assertThat(captured.getStatus()).isEqualTo("pending");
    }

    @Test
    public void cancelReservation_ShouldDeleteReservationAndIncrementRemainingIfApproved() {
        int userId = 1;
        int eventId = 101;
        UserEvent userEvent = new UserEvent();
        userEvent.setStatus("approved");
        Event event = new Event();
        event.setEventId(eventId);
        event.setRemaining(5);

        given(userEventRepository.findByUserIdAndEventId(userId, eventId)).willReturn(userEvent);
        given(eventRepository.findByEventId(eventId)).willReturn(event);

        userEventService.cancelReservation(userId, eventId);

        then(userEventRepository).should().delete(userEvent);
        assertThat(event.getRemaining()).isEqualTo(6);
        then(eventRepository).should().save(event);
    }

    @Test
    public void approveAppoint_ShouldSetStatusToApprovedAndNotify() {
        int userEventId = 1;
        UserEvent userEvent = new UserEvent();
        userEvent.setId(userEventId);
        userEvent.setStatus("pending");

        given(userEventRepository.findUserEventById(userEventId)).willReturn(userEvent);

        String result = userEventService.approveAppoint(userEventId);

        assertThat(userEvent.getStatus()).isEqualTo("approved");
        then(userEventRepository).should().save(userEvent);
        then(emailService).should().registrationApprovedEmail(userEventId);
        assertThat(result).isEqualTo("approved successfully");
    }

    @Test
    public void rejectAppoint_ShouldSetStatusToRejectedAndNotify() {
        int userEventId = 1;
        UserEvent userEvent = new UserEvent();
        userEvent.setId(userEventId);
        userEvent.setStatus("pending");

        given(userEventRepository.findUserEventById(userEventId)).willReturn(userEvent);

        String result = userEventService.rejectAppoint(userEventId);

        assertThat(userEvent.getStatus()).isEqualTo("rejected");
        then(userEventRepository).should().save(userEvent);
        then(emailService).should().registrationRejectedEmail(userEventId);
        assertThat(result).isEqualTo("rejected successfully");
    }

    @Test
    public void getEventOrganizerByUserEventId_ShouldReturnOrganizerId() {
        int userEventId = 1;
        int eventId = 101;
        UserEvent userEvent = new UserEvent();
        userEvent.setEventId(eventId);
        Event event = new Event();
        event.setOrganizerId(10);

        given(userEventRepository.findUserEventById(userEventId)).willReturn(userEvent);
        given(eventRepository.findByEventId(eventId)).willReturn(event);

        int organizerId = userEventService.getEventOrganizerByUserEventId(userEventId);

        assertThat(organizerId).isEqualTo(10);
    }
    // Test cancelReservation when the reservation is approved and event is found
    @Test
    public void cancelReservation_ApprovedAndEventFound_ShouldUpdateRemaining() {
        int userId = 1, eventId = 1;
        UserEvent userEvent = new UserEvent();
        userEvent.setStatus("approved");

        Event event = new Event();
        event.setRemaining(10);

        when(userEventRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(userEvent);
        when(eventRepository.findByEventId(eventId)).thenReturn(event);

        userEventService.cancelReservation(userId, eventId);

        verify(eventRepository).save(event);
        assertEquals(11, event.getRemaining());
    }

    // Test cancelReservation when the reservation is not approved
    @Test
    public void cancelReservation_NotApproved_ShouldNotUpdateRemaining() {
        int userId = 1, eventId = 1;
        UserEvent userEvent = new UserEvent();
        userEvent.setStatus("pending");

        Event event = new Event();
        event.setRemaining(10);

        when(userEventRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(userEvent);

        userEventService.cancelReservation(userId, eventId);

        verify(eventRepository, never()).save(event);
        assertEquals(10, event.getRemaining());
    }

    // Test getEventOrganizerByUserEventId when userEvent and event are found
    @Test
    public void getEventOrganizerByUserEventId_UserEventAndEventFound_ShouldReturnOrganizerId() {
        int userEventId = 1;
        UserEvent userEvent = new UserEvent();
        userEvent.setEventId(1);

        Event event = new Event();
        event.setOrganizerId(100);

        when(userEventRepository.findUserEventById(userEventId)).thenReturn(userEvent);
        when(eventRepository.findByEventId(1)).thenReturn(event);

        int result = userEventService.getEventOrganizerByUserEventId(userEventId);
        assertEquals(100, result);
    }

    // Test getEventOrganizerByUserEventId when userEvent or event not found
    @Test
    public void getEventOrganizerByUserEventId_UserEventOrEventNotFound_ShouldReturnMinusOne() {
        int userEventId = 1;
        when(userEventRepository.findUserEventById(userEventId)).thenReturn(null);  // userEvent not found

        int result = userEventService.getEventOrganizerByUserEventId(userEventId);
        assertEquals(-1, result);
    }
}

