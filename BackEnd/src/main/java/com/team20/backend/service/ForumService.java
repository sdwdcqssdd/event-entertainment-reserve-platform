package com.team20.backend.service;

import com.team20.backend.dto.ForumReplyDTO;
import com.team20.backend.model.forum.ForumReply;
import com.team20.backend.model.forum.ForumTopic;
import com.team20.backend.model.forum.UserLike;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public interface ForumService {
    void saveForumTopic(ForumTopic forumTopic);

    ForumTopic findForumTopicById(int topicId);
    void saveForumReply(ForumReply forumReply);

    void deleteForumTopicById(int topicId);

    void addStar(int userId, int topicId);

    void deleteForumReplyById(int replyId);
    void likeReply(int userId, int replyId);

    List<ForumTopic> getAllForumTopics();

    List<ForumTopic> getStarredTopics(int userId);

    void deleteStar(int userId, int topicId);
    UserLike getUserLike(int userId, int replyId);

    List<ForumTopic> searchForumTopics(String keyword, Date startDate, Date endDate);

    boolean hasSecondaryReplies(int id);
}
