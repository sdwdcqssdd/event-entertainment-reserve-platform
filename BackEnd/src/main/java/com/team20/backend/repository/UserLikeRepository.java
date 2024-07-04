package com.team20.backend.repository;

import com.team20.backend.model.forum.ForumReply;
import com.team20.backend.model.forum.UserLike;
import com.team20.backend.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Integer> {
    Optional<UserLike> findByUserAndReply(Users user, ForumReply forumReply);

    void deleteByUserAndReply(Users user, ForumReply forumReply);
}
