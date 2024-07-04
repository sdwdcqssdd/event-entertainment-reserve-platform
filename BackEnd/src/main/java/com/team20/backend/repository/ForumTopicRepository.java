package com.team20.backend.repository;

import com.team20.backend.model.forum.ForumTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ForumTopicRepository extends JpaRepository<ForumTopic, Integer> {
    ForumTopic findById(int topicId);
    void deleteById(int topicId);

    List<ForumTopic> findByTitleContainingAndPostTimeBetween(String keyword, Date startDate, Date endDate);
}
