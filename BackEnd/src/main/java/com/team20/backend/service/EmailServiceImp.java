package com.team20.backend.service;


import com.team20.backend.model.ChatMessage;
import com.team20.backend.model.Email;
import com.team20.backend.model.Event;
import com.team20.backend.model.UserEvent;
import com.team20.backend.model.forum.ForumReply;
import com.team20.backend.model.forum.ForumTopic;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
public class EmailServiceImp implements EmailService{

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserEventRepository userEventRepository;
    @Autowired
    private ForumTopicRepository forumTopicRepository;
    @Autowired
    private ForumReplyRepository forumReplyRepository;

    @Override
    public List<Email> getEmailsByUserId(int userId) {
        return emailRepository.findByUserID(userId);
    }
    @Override
    public void markEmailAsRead(int emailId) {
        Email email = emailRepository.findById(emailId).orElse(null);
        if (email != null) {
            email.setStatus(Email.EmailStatus.read);
            emailRepository.save(email);
        }
    }
    @Override
    public void markEmailAsDeleted(int emailId){
        Email email = emailRepository.findById(emailId).orElse(null);
        if (email != null) {
            email.setStatus(Email.EmailStatus.deleted);
            emailRepository.save(email);
        }
    }
    @Override
    public void registrationApprovedEmail(int user_event_id) {
        Email email = new Email();
        UserEvent userEvent = userEventRepository.findUserEventById(user_event_id);
        email.setUserID(userEvent.getUserId());
        email.setSubject("活动报名成功通知");
        Users user = usersRepository.findUserByUserId(userEvent.getUserId());
        Event event = eventRepository.findByEventId(userEvent.getEventId());
        email.setBody(user.getUsername()+",您已成功报名"+event.getTitle()+"。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }

    @Override
    public void PrivateChatMessage(ChatMessage chatMessage) {
        Email email = new Email();
        email.setUserID(usersRepository.findUserByUsername(chatMessage.getReceiver()).getUserId());
        email.setSubject(chatMessage.getSender()+"给您发送了私信");
        email.setBody(chatMessage.getContent());
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }

    @Override
    public void registrationRejectedEmail(int user_event_id){
        Email email = new Email();
        UserEvent userEvent = userEventRepository.findUserEventById(user_event_id);
        email.setUserID(userEvent.getUserId());
        email.setSubject("活动报名失败通知");
        Users user = usersRepository.findUserByUserId(userEvent.getUserId());
        Event event = eventRepository.findByEventId(userEvent.getEventId());
        email.setBody(user.getUsername()+",很遗憾的通知您，您未能报名"+event.getTitle()+"。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }

    @Override
    public void eventApprovedEmail(int event_id) {
        Email email = new Email();
        Event event = eventRepository.findByEventId(event_id);
        email.setUserID(event.getOrganizerId());
        email.setSubject("活动审核通过通知");
        Users user = usersRepository.findUserByUserId(event.getOrganizerId());
        email.setBody(user.getUsername()+",您发起的"+event.getTitle()+"已经审核通过。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }

    @Override
    public void eventRejectedEmail(int event_id) {
        Email email = new Email();
        Event event = eventRepository.findByEventId(event_id);
        email.setUserID(event.getOrganizerId());
        email.setSubject("活动审核失败通知");
        Users user = usersRepository.findUserByUserId(event.getOrganizerId());
        email.setBody(user.getUsername()+",您发起的"+event.getTitle()+"未能通过审核。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }

    @Override
    public void followEmail(int followeeId,int followerId){
        Email email = new Email();
        email.setUserID(followeeId);
        email.setSubject("被关注通知");
        Users follower = usersRepository.findUserByUserId(followerId);
        Users followee = usersRepository.findUserByUserId(followeeId);
        email.setBody(followee.getUsername()+",您已被用户："+follower.getUsername()+"关注。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }

    @Override
    public void replyEmail(int replieeId,int replierId){
        Email email = new Email();
        email.setUserID(replieeId);
        email.setSubject("被回复通知");
        Users replier = usersRepository.findUserByUserId(replierId);
        Users repliee = usersRepository.findUserByUserId(replieeId);
        email.setBody(repliee.getUsername()+",您已被用户："+replier.getUsername()+"回复。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }
    @Override
    public void addStarEmail(int userId, int topicId){
        Email email = new Email();
        ForumTopic forumTopic=forumTopicRepository.findById(topicId);
        email.setUserID(forumTopic.getUserId());
        email.setSubject("话题被收藏通知");
        Users liker = usersRepository.findUserByUserId(userId);
        Users likee = usersRepository.findUserByUserId(forumTopic.getUserId());
        email.setBody(likee.getUsername()+",您的帖子被用户："+liker.getUsername()+"收藏了。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }

    @Override
    public void likeReplyEmail(int userId, int replyId){
        Email email = new Email();
        ForumReply forumReply=forumReplyRepository.findById(replyId);
        email.setUserID(forumReply.getUserId());
        email.setSubject("回复被点赞通知");
        Users liker = usersRepository.findUserByUserId(userId);
        Users likee = usersRepository.findUserByUserId(forumReply.getUserId());
        email.setBody(likee.getUsername()+",您的回复被用户："+liker.getUsername()+"点赞了。");
        email.setTime(new Timestamp(System.currentTimeMillis()));
        email.setStatus(Email.EmailStatus.unread);
        emailRepository.save(email);
    }
}
