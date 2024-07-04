package com.team20.backend.service;

import com.team20.backend.dto.ForumReplyDTO;

import com.team20.backend.model.forum.*;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ForumServiceImp implements ForumService{

    @Autowired
    private UserLikeRepository userLikeRepository;
    @Autowired
    private ForumReplyRepository forumReplyRepository;
    @Autowired
    private ForumTopicRepository forumTopicRepository;
    @Autowired
    private TopicStarRepository topicStarRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EmailService emailService;
    @Override
    public void saveForumTopic(ForumTopic forumTopic) {
        forumTopicRepository.save(forumTopic);
    }

    @Override
    public void saveForumReply(ForumReply forumReply) {
        ForumTopic forumTopic=forumReply.getForumTopic();
        emailService.replyEmail((forumReply.getParentId()==0)?forumTopic.getUserId():forumReply.getParentId(),forumReply.getUserId());
        forumReplyRepository.save(forumReply);
    }
    @Override
    public ForumTopic findForumTopicById(int topicId) {
        return forumTopicRepository.findById(topicId);
    }
    @Override
    public void deleteForumTopicById(int topicId) {
        forumTopicRepository.deleteById(topicId);
    }
    @Override
    @Transactional
    public void addStar(int userId, int topicId) {
        TopicStar topicStar = new TopicStar();
        topicStar.setUserId(userId);
        topicStar.setTopicId(topicId);
        emailService.addStarEmail(userId,topicId);
        topicStarRepository.save(topicStar);
    }

    @Override
    public void deleteForumReplyById(int replyId) {
        forumReplyRepository.deleteById(replyId);
        //forumReplyRepository.deleteByParentId(replyId);
    }

    @Override
    public void likeReply(int userId, int replyId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ForumReply forumReply = forumReplyRepository.findById(replyId);

        // 检查用户是否已经点赞
        if (userLikeRepository.findByUserAndReply(user, forumReply).isPresent()) {
            userLikeRepository.deleteByUserAndReply(user, forumReply);
            return;
        }

        // 创建新的点赞记录
        UserLike userLike = new UserLike();
        userLike.setUser(user);
        userLike.setReply(forumReply);
        emailService.likeReplyEmail(userId,replyId);
        userLikeRepository.save(userLike);
    }
    @Override
    public List<ForumTopic> getAllForumTopics() {
        return forumTopicRepository.findAll();
    }
    @Override
    public List<ForumTopic> getStarredTopics(int userId) {
        List<TopicStar> topicStars = topicStarRepository.findByUserId(userId);
        List<ForumTopic> forumTopics = new ArrayList<>();
        for (TopicStar topicStar : topicStars) {
            forumTopics.add(forumTopicRepository.findById(topicStar.getTopicId()));
        }
        return forumTopics;
    }
    @Override
    public void deleteStar(int userId, int topicId) {
        topicStarRepository.deleteByUserIdAndTopicId(userId, topicId);
    }
    @Override
    public UserLike getUserLike(int userId, int replyId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ForumReply forumReply = forumReplyRepository.findById(replyId);
        return userLikeRepository.findByUserAndReply(user, forumReply).orElse(null);
    }

    @Override
    public List<ForumTopic> searchForumTopics(String keyword, Date startDate, Date endDate) {
        return forumTopicRepository.findByTitleContainingAndPostTimeBetween(keyword, startDate, endDate);
    }

    @Override
    public boolean hasSecondaryReplies(int id) {
        return forumReplyRepository.existsByParentId(id);
    }
}
