package com.team20.backend.service;

import com.team20.backend.model.history.Comment;
import com.team20.backend.model.Event;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImp implements CommentService{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private EventService eventService;

    @Override
    public Comment addComment(int userId, int eventId, String content, int rating) {
        Users user = usersService.findUserById(userId);
        Event event = eventService.findByEventId(eventId);

        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }

        Optional<Comment> existingComment = commentRepository.findByUserAndEvent(user, event);
        if (existingComment.isPresent()) {
            throw new IllegalStateException("User has already commented on this event");
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setEvent(event);
        comment.setContent(content);
        comment.setRating(rating);
        comment.setCreated_at(new Date());

        Comment c = commentRepository.save(comment);
        updateEventRating(event);
        return c;
    }

    @Override
    public Comment modifyComment(int userId, int eventId, String content, int rating) {
        Users user = usersService.findUserById(userId);
        Event event = eventService.findByEventId(eventId);

        Optional<Comment> existingComment = commentRepository.findByUserAndEvent(user, event);
        if (existingComment.isEmpty()) {
            throw new IllegalStateException("User has not commented on this event");
        }

        Comment comment = existingComment.get();
        if (comment.getUser().getUserId() != userId) {
            throw new IllegalArgumentException("User can only modify their own comments");
        }

        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }

        comment.setContent(content);
        comment.setRating(rating);
        comment.setCreated_at(new Date()); // Update the timestamp if needed
        Comment c = commentRepository.save(comment);
        updateEventRating(event);
        return c;
    }

    @Override
    public void deleteComment(int userId, int eventId) {
        Users user = usersService.findUserById(userId);
        Event event = eventService.findByEventId(eventId);

        Optional<Comment> existingComment = commentRepository.findByUserAndEvent(user, event);
        if (existingComment.isEmpty()) {
            throw new IllegalStateException("User has not commented on this event");
        }

        Comment comment = existingComment.get();
        if (comment.getUser().getUserId() != userId) {
            throw new IllegalArgumentException("User can only delete their own comments");
        }
        commentRepository.delete(comment);
        updateEventRating(event);
    }


    private void updateEventRating(Event event) {
        List<Comment> comments = commentRepository.getCommentsByEvent(event);
        if (comments.isEmpty()) {
            event.setRating(0);
            //System.out.println("No comments");
        } else {
            double averageRating = comments.stream()
                    .mapToInt(Comment::getRating)
                    .average()
                    .orElse(0);
            //System.out.println(averageRating);
            event.setRating((int)(averageRating + 0.5));
        }
        eventService.saveEvent(event); // 保存事件以更新评分
    }

    @Override
    @Transactional
    public List<Comment> findCommentsByEventId(int eventId) {
        Event event = eventService.findByEventId(eventId);
        return commentRepository.getCommentsByEvent(event);
    }
}

