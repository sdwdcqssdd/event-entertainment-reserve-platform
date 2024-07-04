package com.team20.backend.dto;

import com.team20.backend.model.forum.ForumTopic;

import java.util.List;

public class TopicDTO {
    private TopicUserDTO topic;
    private List<ForumReplyDTO> replies;

    public TopicDTO(TopicUserDTO topic, List<ForumReplyDTO> replies) {
        this.topic = topic;
        this.replies = replies;
    }

    public TopicUserDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicUserDTO topic) {
        this.topic = topic;
    }

    public List<ForumReplyDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<ForumReplyDTO> replies) {
        this.replies = replies;
    }
}
