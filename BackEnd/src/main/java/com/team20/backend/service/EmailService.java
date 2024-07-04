package com.team20.backend.service;

import com.team20.backend.model.ChatMessage;
import com.team20.backend.model.Email;

import java.util.List;

public interface EmailService {
    List<Email> getEmailsByUserId(int userId);
    void markEmailAsRead(int emailId);
    void markEmailAsDeleted(int emailId);
    void registrationApprovedEmail(int user_event_id);
    void registrationRejectedEmail(int user_event_id);
    void eventApprovedEmail(int event_id);
    void eventRejectedEmail(int event_id);
    void followEmail(int followeeId,int followerId);
    void replyEmail(int replieeId,int replierId);
    void PrivateChatMessage(ChatMessage chatMessage);
    void addStarEmail(int userId, int topicId);
    void likeReplyEmail(int userId, int replyId);
}
