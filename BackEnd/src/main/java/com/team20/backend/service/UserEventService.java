package com.team20.backend.service;

import com.team20.backend.model.UserEvent;

import java.util.List;

// TODO: 未测试

public interface UserEventService {
    void reserveEvent(int userId, int eventId);
    void cancelReservation(int userId, int eventId);

    String approveAppoint(int user_event_id);

    int getEventOrganizerByUserEventId(int user_event_id);

    List<UserEvent> findUserEventByUserId(int userId);

    List<UserEvent> findUserEventByStatusAndEventId(String pending, int eventId);

    String rejectAppoint(int user_event_id);
}
