package com.team20.backend.repository;

import com.team20.backend.model.history.Comment;
import com.team20.backend.model.Event;
import com.team20.backend.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByUserAndEvent(Users user, Event event);

    List<Comment> getCommentsByEvent(Event event);
}
