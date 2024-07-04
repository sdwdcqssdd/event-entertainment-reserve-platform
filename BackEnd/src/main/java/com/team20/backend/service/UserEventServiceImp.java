package com.team20.backend.service;

import com.team20.backend.repository.EventRepository;
import com.team20.backend.repository.UserEventRepository;
import com.team20.backend.model.UserEvent;
import com.team20.backend.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: 未测试

/**
 * AI-generated-content
 * tool: chatGpt
 * version: 3.5
 * usage: 生成大致的框架
 */

@Service
public class UserEventServiceImp implements UserEventService {

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;
    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage: 用于预约事件
     * 我修改了setStatus
     */
    @Override
    public void reserveEvent(int userId, int eventId) {
        UserEvent userEvent = new UserEvent();
        userEvent.setUserId(userId);
        userEvent.setEventId(eventId);
        userEvent.setStatus("pending");
        userEventRepository.save(userEvent);
    }

    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage: 取消预约
     * 大幅修改，增加了当删除的预约已审核通过时，相关event的remaining应该+1的判断
     */

    @Override
    public void cancelReservation(int userId, int eventId) {
        UserEvent userEvent = userEventRepository.findByUserIdAndEventId(userId, eventId);

        if(userEvent != null && userEvent.getStatus().equals("approved")) {
            userEventRepository.delete(userEvent);
            Event event = eventRepository.findByEventId(eventId);
            if (event != null) {
                event.setRemaining(event.getRemaining() + 1);
                eventRepository.save(event);
            }
        } else if(userEvent != null) {
            userEventRepository.delete(userEvent);
        }
    }

    @Override
    public List<UserEvent> findUserEventByStatusAndEventId(String pending, int eventId) {
        return userEventRepository.findUserEventByStatusAndEventId(pending, eventId);
    }

    @Override
    public List<UserEvent> findUserEventByUserId(int userId) {
        return userEventRepository.findUserEventByUserId(userId);
    }
    @Override
    public String approveAppoint(int user_event_id) {
        UserEvent userEvent = userEventRepository.findUserEventById(user_event_id);
        userEvent.setStatus("approved");
        userEventRepository.save(userEvent);
        emailService.registrationApprovedEmail(user_event_id);
        return "approved successfully";
    }

    @Override
    public String rejectAppoint(int user_event_id) {
        UserEvent userEvent = userEventRepository.findUserEventById(user_event_id);
        userEvent.setStatus("rejected");
        userEventRepository.save(userEvent);
        emailService.registrationRejectedEmail(user_event_id);
        return "rejected successfully";
    }

    @Override
    public int getEventOrganizerByUserEventId(int userEventId) {
        UserEvent userEvent = userEventRepository.findUserEventById(userEventId);
        if (userEvent != null) {
            int eventId = userEvent.getEventId();
            Event event = eventRepository.findByEventId(eventId);
            if (event != null) {
                return event.getOrganizerId();
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
