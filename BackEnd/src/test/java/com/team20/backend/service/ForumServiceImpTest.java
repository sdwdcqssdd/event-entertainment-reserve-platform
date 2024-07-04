package com.team20.backend.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.team20.backend.model.forum.ForumReply;
import com.team20.backend.model.forum.ForumTopic;
import com.team20.backend.model.forum.TopicStar;
import com.team20.backend.model.forum.UserLike;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class ForumServiceImpTest {
    @Mock
    private ForumTopicRepository forumTopicRepository;
    @Mock
    private ForumReplyRepository forumReplyRepository;
    @Mock
    private TopicStarRepository topicStarRepository;
    @Mock
    private UserLikeRepository userLikeRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private ForumServiceImp forumService;
    @Test
    public void saveForumTopic_ShouldSaveTopic() {
        ForumTopic topic = new ForumTopic();
        topic.setTitle("New Topic");
        topic.setContent("This is a new discussion topic.");

        forumService.saveForumTopic(topic);

        then(forumTopicRepository).should().save(topic);
    }
    @Test
    public void saveForumReply_ShouldSaveReplyAndSendEmail() {
        ForumReply reply = new ForumReply();
        reply.setUserId(1);
        reply.setParentId(0); // Assume this is a top-level reply
        ForumTopic topic = new ForumTopic();
        topic.setId(1);
        topic.setUserId(2); // Topic creator
        reply.setForumTopic(topic);

        forumService.saveForumReply(reply);

        then(forumReplyRepository).should().save(reply);
        then(emailService).should().replyEmail(2, 1); // Topic creator gets an email
    }
    @Test
    public void deleteForumTopicById_ShouldDeleteTopic() {
        int topicId = 1;

        forumService.deleteForumTopicById(topicId);

        then(forumTopicRepository).should().deleteById(topicId);
    }
    @Test
    public void addStar_ShouldSaveStarAndSendEmail() {
        int userId = 1;
        int topicId = 1;

        forumService.addStar(userId, topicId);

        then(topicStarRepository).should().save(any(TopicStar.class));
        then(emailService).should().addStarEmail(userId, topicId);
    }
    @Test
    public void deleteStar_ShouldDeleteStar() {
        int userId = 1;
        int topicId = 1;

        forumService.deleteStar(userId, topicId);

        then(topicStarRepository).should().deleteByUserIdAndTopicId(userId, topicId);
    }
    @Test
    public void likeReply_ShouldSaveLikeAndSendEmail() {
        int userId = 1;
        int replyId = 1;
        Users user = new Users();
        user.setUserId(userId);
        ForumReply reply = new ForumReply();
        reply.setId(replyId);

        given(usersRepository.findById(userId)).willReturn(Optional.of(user));
        given(forumReplyRepository.findById(replyId)).willReturn(reply);
        given(userLikeRepository.findByUserAndReply(user, reply)).willReturn(Optional.empty());

        forumService.likeReply(userId, replyId);

        then(userLikeRepository).should().save(any(UserLike.class));
        then(emailService).should().likeReplyEmail(userId, replyId);
    }
    @Test
    public void getAllForumTopics_ShouldReturnAllTopics() {
        List<ForumTopic> topics = Arrays.asList(new ForumTopic(), new ForumTopic());

        given(forumTopicRepository.findAll()).willReturn(topics);

        List<ForumTopic> result = forumService.getAllForumTopics();

        assertThat(result).isEqualTo(topics);
    }
    @Test
    public void getStarredTopics_ShouldReturnStarredTopics() {
        int userId = 1;
        List<TopicStar> stars = Arrays.asList(new TopicStar());
        stars.get(0).setTopicId(1);
        ForumTopic topic = new ForumTopic();
        topic.setId(1);

        given(topicStarRepository.findByUserId(userId)).willReturn(stars);
        given(forumTopicRepository.findById(1)).willReturn(topic);

        List<ForumTopic> result = forumService.getStarredTopics(userId);

        assertTrue(result.contains(topic));
    }

}

