package com.team20.backend.service;

import com.team20.backend.model.ChatMessage;
import com.team20.backend.model.Email;
import com.team20.backend.model.UserEvent;
import com.team20.backend.model.forum.ForumReply;
import com.team20.backend.model.forum.ForumTopic;
import com.team20.backend.model.user.Users;
import com.team20.backend.model.Event;
import com.team20.backend.repository.*;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;


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
public class EmailServiceImpTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserEventRepository userEventRepository;
    @Mock
    private ForumTopicRepository forumTopicRepository;
    @Mock
    private ForumReplyRepository forumReplyRepository;

    @InjectMocks
    private EmailServiceImp emailService;
    @Captor
    private ArgumentCaptor<Email> emailCaptor;

    @Test
    public void getEmailsByUserId_ShouldReturnEmailList() {
        when(emailRepository.findByUserID(anyInt())).thenReturn(Arrays.asList(new Email()));
        List<Email> emails = emailService.getEmailsByUserId(1);
        assertThat(emails).isNotNull();
        assertThat(emails.size()).isEqualTo(1);
    }

    @Test
    public void markEmailAsRead_ShouldUpdateStatus() {
        Email email = new Email();
        email.setStatus(Email.EmailStatus.unread);
        given(emailRepository.findById(anyInt())).willReturn(java.util.Optional.of(email));
        emailService.markEmailAsRead(1);
        then(emailRepository).should(times(1)).save(emailCaptor.capture());
        assertThat(emailCaptor.getValue().getStatus()).isEqualTo(Email.EmailStatus.read);
    }

    @Test
    public void registrationApprovedEmail_ShouldSendEmail() {
        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(1);
        userEvent.setEventId(2);
        Users user = new Users();
        user.setUsername("John Doe");
        Event event = new Event();
        event.setTitle("Spring Boot Workshop");

        given(userEventRepository.findUserEventById(anyInt())).willReturn(userEvent);
        given(usersRepository.findUserByUserId(anyInt())).willReturn(user);
        given(eventRepository.findByEventId(anyInt())).willReturn(event);

        emailService.registrationApprovedEmail(1);

        then(emailRepository).should().save(emailCaptor.capture());
        Email capturedEmail = emailCaptor.getValue();
        assertThat(capturedEmail.getSubject()).contains("活动报名成功通知");
        assertThat(capturedEmail.getBody()).contains("John Doe", "Spring Boot Workshop");
    }
    @Test
    public void markEmailAsDeleted_ShouldUpdateStatus() {
        Email email = new Email();
        email.setStatus(Email.EmailStatus.unread);
        given(emailRepository.findById(anyInt())).willReturn(java.util.Optional.of(email));

        emailService.markEmailAsDeleted(1);

        then(emailRepository).should(times(1)).save(emailCaptor.capture());
        assertThat(emailCaptor.getValue().getStatus()).isEqualTo(Email.EmailStatus.deleted);
    }

    @Test
    public void registrationRejectedEmail_ShouldSendEmail() {
        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(1);
        userEvent.setEventId(2);
        Users user = new Users();
        user.setUsername("Jane Doe");
        Event event = new Event();
        event.setTitle("Spring Boot Workshop");

        given(userEventRepository.findUserEventById(anyInt())).willReturn(userEvent);
        given(usersRepository.findUserByUserId(anyInt())).willReturn(user);
        given(eventRepository.findByEventId(anyInt())).willReturn(event);

        emailService.registrationRejectedEmail(1);

        then(emailRepository).should().save(emailCaptor.capture());
        Email capturedEmail = emailCaptor.getValue();
        assertThat(capturedEmail.getSubject()).contains("活动报名失败通知");
        assertThat(capturedEmail.getBody()).contains("Jane Doe", "Spring Boot Workshop");
    }

    @Test
    public void eventApprovedEmail_ShouldSendNotification() {
        Event event = new Event();
        event.setTitle("Annual Conference");
        event.setOrganizerId(1);
        Users user = new Users();
        user.setUsername("JohnDoe");
        user.setUserId(1);

        given(eventRepository.findByEventId(anyInt())).willReturn(event);
        given(usersRepository.findUserByUserId(anyInt())).willReturn(user);

        emailService.eventApprovedEmail(1);

        then(emailRepository).should().save(argThat(email ->
                email.getSubject().equals("活动审核通过通知") &&
                        email.getBody().contains("JohnDoe,您发起的Annual Conference已经审核通过。") &&
                        email.getStatus() == Email.EmailStatus.unread
        ));
    }

    @Test
    public void eventRejectedEmail_ShouldSendNotification() {
        Event event = new Event();
        event.setTitle("Annual Conference");
        event.setOrganizerId(1);
        Users user = new Users();
        user.setUsername("JohnDoe");
        user.setUserId(1);

        given(eventRepository.findByEventId(anyInt())).willReturn(event);
        given(usersRepository.findUserByUserId(anyInt())).willReturn(user);

        emailService.eventRejectedEmail(1);

        then(emailRepository).should().save(argThat(email ->
                email.getSubject().equals("活动审核失败通知") &&
                        email.getBody().contains("JohnDoe,您发起的Annual Conference未能通过审核。") &&
                        email.getStatus() == Email.EmailStatus.unread
        ));
    }
    @Test
    public void privateChatMessage_ShouldSendEmail() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setReceiver("JaneReceiver");
        chatMessage.setSender("JohnSender");
        chatMessage.setContent("Hello, Jane!");
        Users receiver = new Users();
        receiver.setUserId(1);
        receiver.setUsername("JaneReceiver");

        given(usersRepository.findUserByUsername(chatMessage.getReceiver())).willReturn(receiver);

        emailService.PrivateChatMessage(chatMessage);

        then(emailRepository).should().save(emailCaptor.capture());
        Email capturedEmail = emailCaptor.getValue();
        assertThat(capturedEmail.getSubject()).contains("给您发送了私信");
        assertThat(capturedEmail.getBody()).contains("Hello, Jane!");
    }

    @Test
    public void followEmail_ShouldNotifyFollowee() {
        Users follower = new Users();
        follower.setUserId(1);
        follower.setUsername("JohnFollower");
        Users followee = new Users();
        followee.setUserId(2);
        followee.setUsername("JaneFollowee");

        given(usersRepository.findUserByUserId(1)).willReturn(follower);
        given(usersRepository.findUserByUserId(2)).willReturn(followee);

        emailService.followEmail(2, 1);

        then(emailRepository).should().save(emailCaptor.capture());
        Email capturedEmail = emailCaptor.getValue();
        assertThat(capturedEmail.getSubject()).contains("被关注通知");
        assertThat(capturedEmail.getBody()).contains("JaneFollowee", "JohnFollower");
    }

    @Test
    public void replyEmail_ShouldNotifyRepliee() {
        Users replier = new Users();
        replier.setUserId(1);
        replier.setUsername("JohnReplier");
        Users repliee = new Users();
        repliee.setUserId(2);
        repliee.setUsername("JaneRepliee");

        given(usersRepository.findUserByUserId(1)).willReturn(replier);
        given(usersRepository.findUserByUserId(2)).willReturn(repliee);

        emailService.replyEmail(2, 1);

        then(emailRepository).should().save(emailCaptor.capture());
        Email capturedEmail = emailCaptor.getValue();
        assertThat(capturedEmail.getSubject()).contains("被回复通知");
        assertThat(capturedEmail.getBody()).contains("JaneRepliee", "JohnReplier");
    }

    @Test
    public void addStarEmail_ShouldSendNotification() {
        Users liker = new Users();
        liker.setUsername("JaneDoe");
        liker.setUserId(2);
        ForumTopic forumTopic = new ForumTopic();
        forumTopic.setUserId(1);
        Users likee = new Users();
        likee.setUsername("JohnDoe");
        likee.setUserId(1);

        given(forumTopicRepository.findById(100)).willReturn(forumTopic);
        given(usersRepository.findUserByUserId(2)).willReturn(liker);
        given(usersRepository.findUserByUserId(1)).willReturn(likee);

        emailService.addStarEmail(2, 100);

        then(emailRepository).should().save(argThat(email ->
                email.getSubject().equals("话题被收藏通知") &&
                        email.getBody().contains("JohnDoe,您的帖子被用户：JaneDoe收藏了。") &&
                        email.getStatus() == Email.EmailStatus.unread
        ));
    }

    // Test for likeReplyEmail method
    @Test
    public void likeReplyEmail_ShouldSendNotification() {
        Users liker = new Users();
        liker.setUsername("JaneDoe");
        liker.setUserId(2);
        Users likee = new Users();
        likee.setUsername("JohnDoe");
        likee.setUserId(1);
        ForumReply forumReply = new ForumReply();
        forumReply.setUserId(1);
        forumReply.setContent("Thank you for your feedback!");

        given(forumReplyRepository.findById(anyInt())).willReturn(forumReply);
        given(usersRepository.findUserByUserId(2)).willReturn(liker);
        given(usersRepository.findUserByUserId(1)).willReturn(likee);


        emailService.likeReplyEmail(2, 200);

        then(emailRepository).should().save(argThat(email ->
                email.getSubject().equals("回复被点赞通知") &&
                        email.getBody().contains("您的回复被用户：JaneDoe点赞了。") &&
                        email.getStatus() == Email.EmailStatus.unread
        ));
    }
}












