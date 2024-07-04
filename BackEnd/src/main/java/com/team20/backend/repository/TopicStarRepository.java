package com.team20.backend.repository;

import com.team20.backend.model.forum.TopicStar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicStarRepository extends JpaRepository<TopicStar, Integer> {
    List<TopicStar> findByUserId(int userId);

    void deleteByUserIdAndTopicId(int userId, int topicId);
}
