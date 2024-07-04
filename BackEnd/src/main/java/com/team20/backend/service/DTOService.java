package com.team20.backend.service;

import com.team20.backend.dto.*;
import com.team20.backend.model.Event;
import com.team20.backend.model.forum.ForumReply;

import java.util.List;

public interface DTOService {
    EventCommentDTO getEventCommentDTO(int eventId);
    UserDTO getUserDTO(int userId);
    ForumReplyDTO getForumReplyDTO(int replyId);
    TopicDTO getTopicDTO(int topicId);
    EventDTO getEventDTO(int eventID);

    EventDTO convertToDTO(Event event);
    ForumReplyDTO convertToDTO(ForumReply forumReply);
    List<ForumReplyDTO> getForumReplies(int topicID);

    List<ForumReplyDTO> getForumRepliesByReplyId(int replyId);
    TopicUserDTO getTopicUserDTO(int topicId);
}
