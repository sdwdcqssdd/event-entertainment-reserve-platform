package com.team20.backend.repository;

import com.team20.backend.model.forum.ForumReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumReplyRepository extends JpaRepository<ForumReply, Integer> {
    void deleteByParentId(int replyId);

    List<ForumReply> findByForumTopicId(int topicId);
    ForumReply findById(int replyId);

    List<ForumReply> findByParentId(int replyId);

    boolean existsByParentId(int id);
}
