package com.team20.backend.service;

import com.team20.backend.model.user.Follow;

import java.util.List;

public interface FollowService {
    void follow(int followerId, int followeeId);
    void unfollow(int followerId, int followeeId);
    List<Follow> getFollowers(int userId);
    List<Follow> getFollowees(int userId);

    Follow getByFollowerIdAndFolloweeId(int userId, int user_id);
}
