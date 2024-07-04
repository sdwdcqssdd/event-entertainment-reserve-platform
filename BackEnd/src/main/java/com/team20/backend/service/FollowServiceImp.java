package com.team20.backend.service;

import com.team20.backend.model.user.Follow;
import com.team20.backend.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImp implements FollowService{
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public void follow(int followerId, int followeeId) {
        if (followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId) != null) return;
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFolloweeId(followeeId);
        followRepository.save(follow);
        emailService.followEmail(followeeId,followerId);
    }

    @Override
    public void unfollow(int followerId, int followeeId) {
        if (followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId) == null) return;
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    @Override
    public List<Follow> getFollowers(int userId) {
        return followRepository.findByFolloweeId(userId);
    }

    @Override
    public List<Follow> getFollowees(int userId) {
        return followRepository.findByFollowerId(userId);
    }

    @Override
    public Follow getByFollowerIdAndFolloweeId(int userId, int user_id) {
        return followRepository.findByFollowerIdAndFolloweeId(userId, user_id);
    }
}
