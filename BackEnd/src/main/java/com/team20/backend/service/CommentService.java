package com.team20.backend.service;

import com.team20.backend.model.history.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(int userId, int eventId, String content, int rating);
    Comment modifyComment(int userId, int eventId, String content, int rating);
    void deleteComment(int userId, int eventId);

    List<Comment> findCommentsByEventId(int eventId);
}
