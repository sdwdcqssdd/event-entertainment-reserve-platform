package com.team20.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.mockito.BDDMockito.*;

import com.team20.backend.dto.EventQuery;
import com.team20.backend.model.Event;
import com.team20.backend.repository.EventRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class EventServiceImpTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EventServiceImp eventService;

    @Test
    public void findEventsAfterCurrentDate_ShouldReturnEvents() {
        Event event1 = new Event();
        event1.setDate(new Date(System.currentTimeMillis() + 100000)); // Future date
        event1.setType(Event.EventType.approved);

        given(eventRepository.findAll()).willReturn(Arrays.asList(event1));

        List<Event> events = eventService.findEventsAfterCurrentDate();

        assertThat(events).containsExactly(event1);
    }

    @Test
    public void approveEvent_ShouldChangeStatusAndSendEmail() {
        Event event = new Event();
        event.setEventId(1);
        event.setType(Event.EventType.pending);

        given(eventRepository.findByEventId(anyInt())).willReturn(event);

        String result = eventService.approveEvent(1);

        then(eventRepository).should().save(event);
        then(emailService).should().eventApprovedEmail(1);
        assertThat(event.getType()).isEqualTo(Event.EventType.approved);
        assertThat(result).isEqualTo("approved successfully");
    }

    @Test
    public void rejectEvent_ShouldChangeStatusAndSendEmail() {
        Event event = new Event();
        event.setEventId(1);
        event.setType(Event.EventType.pending);

        given(eventRepository.findByEventId(anyInt())).willReturn(event);

        String result = eventService.rejectEvent(1);

        then(eventRepository).should().save(event);
        then(emailService).should().eventRejectedEmail(1);
        assertThat(event.getType()).isEqualTo(Event.EventType.rejected);
        assertThat(result).isEqualTo("rejected successfully");
    }

    @Test
    public void findPendingEvents_ShouldReturnOnlyPending() {
        Event event1 = new Event();
        event1.setType(Event.EventType.pending);
        Event event2 = new Event();
        event2.setType(Event.EventType.approved);

        given(eventRepository.findByType(Event.EventType.pending)).willReturn(Arrays.asList(event1));

        List<Event> events = eventService.findPendingEvents();

        assertThat(events).containsExactly(event1);
        assertThat(events).doesNotContain(event2);
    }

    @Test
    public void findEventsOnDate_ShouldReturnEventsOnSpecificDate() {
        Date specificDate = new Date();
        Event event1 = new Event();
        event1.setDate(specificDate);

        given(eventRepository.findByDate(any(Date.class))).willReturn(Arrays.asList(event1));

        List<Event> events = eventService.findEventsOnDate(specificDate);

        assertThat(events).containsExactly(event1);
    }

    @Test
    public void findEventsByQuery_ShouldReturnFilteredEvents() {
        EventQuery query = new EventQuery(); // Assuming EventQuery has some fields to filter on
        Event event1 = new Event();
        event1.setTitle("Java Conference");
        Event event2 = new Event();
        event2.setTitle("Python Workshop");

        given(eventRepository.findByQuery(query)).willReturn(Arrays.asList(event1, event2));

        List<Event> results = eventService.findEventsByQuery(query);

        assertThat(results).containsExactly(event1, event2);
    }

    @Test
    public void recommend_ShouldReturnSortedEventsBasedOnUserPreferences() {
        Event event1 = new Event();
        event1.setEventId(1);
        event1.setDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // Sets the event date to one day in the future
        event1.setType(Event.EventType.approved);

        Event event2 = new Event();
        event2.setEventId(2);
        event2.setDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // Sets the event date to two days in the future
        event2.setType(Event.EventType.approved);

        given(eventRepository.findAll()).willReturn(Arrays.asList(event1, event2));

        List<Integer> appointedEvents = Arrays.asList(1);
        double[] usrImg = {1.0, 2.0}; // Assuming this is a simplified user feature array

        given(eventRepository.findAll()).willReturn(Arrays.asList(event1, event2));
        given(eventService.findEventsAfterCurrentDate()).willReturn(Arrays.asList(event1, event2));

        List<Event> results = eventService.recommend(appointedEvents, usrImg);

        // Assume a simple comparator for this example; your actual comparator may need to be more complex
        Comparator<Event> scoreComparator = Comparator.comparingInt(Event::getEventId);
        results.sort(scoreComparator);

        assertThat(results).containsExactly(event2);
    }
    @Test
    public void saveEvent_ShouldSaveEventCorrectly() {
        Event event = new Event();
        event.setEventId(1);
        event.setTitle("Java Conference");

        eventService.saveEvent(event);

        then(eventRepository).should().save(event);
    }
    @Test
    public void findByEventId_ShouldReturnEvent() {
        Event event = new Event();
        event.setEventId(1);
        event.setTitle("Java Conference");

        given(eventRepository.findByEventId(1)).willReturn(event);

        Event result = eventService.findByEventId(1);

        assertThat(result).isEqualTo(event);
    }
    @Test
    public void featureVector_ShouldCalculateFeaturesCorrectly() {
        Event event = new Event();
        event.setEventId(1);
        event.setVenueId(1);
        event.setCategoryId(2);
        event.setStartTime(new Date(0)); // midnight, 1970-01-01
        event.setEndTime(new Date(1000 * 3600 * 10)); // 10 hours later, not 2 hours

        double[] feature = new double[17];
        eventService.featureVector(event, feature);

        assertThat(feature[1]).isEqualTo(1.0); // Assuming category index starts at 0 and increments in feature array
        assertThat(feature[6]).isEqualTo(1.0); // Assuming venue index starts at 6 and increments in feature array
        assertThat(feature[15]).isEqualTo(0.3333333333333333); // Start time at midnight
        assertThat(feature[16]).isEqualTo(0.75, within(0.00001)); // End time 10 hours later as a fraction of the day
    }



}
