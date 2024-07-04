package com.team20.backend.dto;

import com.team20.backend.model.history.Comment;
import com.team20.backend.model.Event;

import java.util.List;

/**
 *同时传递活动和评论
 */
public class EventCommentDTO {


    private EventDTO event;

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    private List<Comment> comments;

    // 构造函数
    public EventCommentDTO(EventDTO event, List<Comment> comments) {
        this.event = event;
        this.comments = comments;
    }

}