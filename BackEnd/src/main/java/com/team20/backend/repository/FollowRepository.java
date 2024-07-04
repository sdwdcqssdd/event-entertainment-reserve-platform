package com.team20.backend.repository;

import com.team20.backend.model.user.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Follow findByFollowerIdAndFolloweeId(int followerId, int followeeId);

    List<Follow> findByFolloweeId(int userId);

    List<Follow> findByFollowerId(int userId);
}
