package com.team20.backend.dto;

import com.team20.backend.model.UserEvent;

public class UserEventDTO {
    private UserEvent userEvent;
    private EventDTO eventDTO;

    public UserEventDTO(UserEvent userEvent, EventDTO eventDTO) {
        this.userEvent = userEvent;
        this.eventDTO = eventDTO;
    }

    public UserEvent getUserEvent() {
        return userEvent;
    }

    public void setUserEvent(UserEvent userEvent) {
        this.userEvent = userEvent;
    }

    public EventDTO getEventDTO() {
        return eventDTO;
    }

    public void setEventDTO(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
    }
}
