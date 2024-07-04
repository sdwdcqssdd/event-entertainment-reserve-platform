package com.team20.backend.service;

import java.util.Date;
import java.util.List;

import com.team20.backend.model.Event;
import com.team20.backend.dto.EventQuery;

public interface EventService {
    List<Event> findEventsAfterCurrentDate();
    void saveEvent(Event event);
    List<Event> findEventsOnDate(Date date);
    List<Event> findPendingEvents();

    String approveEvent(int event_id);

    List<Event> findEventsByQuery(EventQuery query);

    Event findByEventId(int eventId);

    List<Event> recommend(List<Integer> appointedEvents, double[] usrImg);

    void featureVector(Event event, double[] feature);

    String rejectEvent(int event_id);
}

