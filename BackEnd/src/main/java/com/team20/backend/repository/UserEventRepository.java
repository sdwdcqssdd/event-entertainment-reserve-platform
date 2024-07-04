package com.team20.backend.repository;

import com.team20.backend.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO: 未测试

/**
 * AI-generated-content
 * tool: chatGpt
 * version: 3.5
 * usage: 避免重复劳动，这个类与EventRepository高度相似
 */
@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Integer> {
    UserEvent findByUserIdAndEventId(int userId, int eventId);

    UserEvent findUserEventById(int user_event_id);

    List<UserEvent> findUserEventByUserId(int userId);

    List<UserEvent> findUserEventByStatusAndEventId(String pending, int eventId);
}
