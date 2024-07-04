package com.team20.backend.service;

import com.team20.backend.dto.*;
import com.team20.backend.model.Event;
import com.team20.backend.model.forum.ForumReply;
import com.team20.backend.model.history.Comment;
import com.team20.backend.repository.ForumReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DTOServiceImp implements DTOService{
    @Autowired
    private UsersService usersService;
    @Autowired
    private EventService eventService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AvatarService avatarService;
    @Autowired
    private ForumService forumService;
    @Autowired
    private ForumReplyRepository forumReplyRepository;
    @Override
    public UserDTO getUserDTO(int userId) {
        return new UserDTO(usersService.findUserById(userId), avatarService.findApprovedAvatarByUserId(userId).getAvatarData());
    }

    @Override
    public EventCommentDTO getEventCommentDTO(int eventId) {
        return new EventCommentDTO(getEventDTO(eventId),commentService.findCommentsByEventId(eventId));
    }
    @Override
    public EventDTO getEventDTO(int eventId) {
        Event event = eventService.findByEventId(eventId);
        UserDTO user = getUserDTO(event.getOrganizerId());
        return new EventDTO(user, event);
    }
    @Override
    public ForumReplyDTO getForumReplyDTO(int replyId) {
        ForumReply forumReply = forumReplyRepository.findById(replyId);
        return new ForumReplyDTO(forumReply, getUserDTO(forumReply.getUserId()));
    }
    @Override
    public TopicUserDTO getTopicUserDTO(int topicId) {
        return new TopicUserDTO(forumService.findForumTopicById(topicId), getUserDTO(forumService.findForumTopicById(topicId).getUserId()));
    }
    @Override
    public TopicDTO getTopicDTO(int topicId) {
        return new TopicDTO(getTopicUserDTO(topicId), getForumReplies(topicId));
    }
    @Override
    public EventDTO convertToDTO(Event event) {
        UserDTO user = getUserDTO(event.getOrganizerId());
        return new EventDTO(user, event);
    }
    @Override
    public ForumReplyDTO convertToDTO(ForumReply forumReply) {
        return new ForumReplyDTO(forumReply, getUserDTO(forumReply.getUserId()));
    }

    @Override
    public List<ForumReplyDTO> getForumReplies(int topicId) {
        List<ForumReply> forumReplies = forumReplyRepository.findByForumTopicId(topicId);
        return forumReplies.stream().
                filter(forumReply -> forumReply.getParentId() == 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ForumReplyDTO> getForumRepliesByReplyId(int replyId) {
        List<ForumReply> forumReplies = forumReplyRepository.findByParentId(replyId);
        return forumReplies.stream().
                map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
