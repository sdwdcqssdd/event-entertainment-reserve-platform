package com.team20.backend.dto;

import com.team20.backend.model.forum.ForumTopic;

public class TopicUserDTO {
    private ForumTopic topic;
    private UserDTO user;

    public ForumTopic getTopic() {
        return topic;
    }

    public void setTopic(ForumTopic topic) {
        this.topic = topic;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public TopicUserDTO(ForumTopic topic, UserDTO user) {
        this.topic = topic;
        this.user = user;
    }
}
