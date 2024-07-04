package com.team20.backend.model;

import jakarta.persistence.*;

// TODO: 未测试

/**
 * AI-generated-content
 * tool: chatGpt
 * version: 3.5
 * usage: 生成UserEvent类，我添加了get与set方法，并将所有id由long改为int
 * 根据实际的表更改了@column的名字
 */
@Entity
@Table(name = "user_event")
public class UserEvent {
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getStatus() {
        return status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_event_id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "event_id")
    private int eventId;

    @Column(name = "status")
    private String status;

}
