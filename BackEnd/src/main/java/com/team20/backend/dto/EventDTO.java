package com.team20.backend.dto;

import com.team20.backend.model.Event;

public class EventDTO {
    private UserDTO user;
    private Event event;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EventDTO(UserDTO user, Event event) {
        this.user = user;
        this.event = event;
    }
}
