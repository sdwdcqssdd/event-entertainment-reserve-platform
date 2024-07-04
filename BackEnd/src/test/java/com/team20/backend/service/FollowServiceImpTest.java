package com.team20.backend.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.team20.backend.model.user.Follow;
import com.team20.backend.repository.FollowRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class FollowServiceImpTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FollowServiceImp followService;

    @Test
    public void follow_ShouldCreateFollowAndSendEmail() {
        int followerId = 1;
        int followeeId = 2;

        given(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).willReturn(null);

        followService.follow(followerId, followeeId);

        then(followRepository).should().save(any(Follow.class));
        then(emailService).should().followEmail(followeeId, followerId);
    }
    @Test
    public void unfollow_ShouldDeleteFollow() {
        int followerId = 1;
        int followeeId = 2;
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFolloweeId(followeeId);

        given(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).willReturn(follow);

        followService.unfollow(followerId, followeeId);

        then(followRepository).should().delete(follow);
    }
    @Test
    public void getFollowers_ShouldReturnFollowersList() {
        int userId = 1;
        List<Follow> followers = Arrays.asList(new Follow());

        given(followRepository.findByFolloweeId(userId)).willReturn(followers);

        List<Follow> result = followService.getFollowers(userId);

        assertThat(result).isEqualTo(followers);
    }
    @Test
    public void getFollowees_ShouldReturnFolloweesList() {
        int userId = 1;
        List<Follow> followees = Arrays.asList(new Follow());

        given(followRepository.findByFollowerId(userId)).willReturn(followees);

        List<Follow> result = followService.getFollowees(userId);

        assertThat(result).isEqualTo(followees);
    }
    @Test
    public void getByFollowerIdAndFolloweeId_ShouldReturnFollow() {
        int followerId = 1;
        int followeeId = 2;
        Follow follow = new Follow();

        given(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)).willReturn(follow);

        Follow result = followService.getByFollowerIdAndFolloweeId(followerId, followeeId);

        assertThat(result).isEqualTo(follow);
    }


}

