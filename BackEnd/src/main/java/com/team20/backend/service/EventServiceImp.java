package com.team20.backend.service;

import com.team20.backend.dto.EventQuery;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.team20.backend.repository.EventRepository;
import com.team20.backend.model.Event;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImp implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EmailService emailService;


    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage:生成findEvents方法，进行了小修改（具体如下）
     */
    
    //fix bug: 修复了返回事件无视事件类型的问题
    @Override
    @Transactional
    //@Cacheable(value = "eventsAfterCurrentDate", unless = "#result.size() == 0")
    public List<Event> findEventsAfterCurrentDate() {
        Date currentDate = new Date();
        return eventRepository.findAll().stream()
                .filter(event -> event.getDate().after(currentDate))
                .filter(event -> Event.EventType.approved.equals(event.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public List<Event> findEventsOnDate(Date date) {
        return eventRepository.findByDate(date);
    }

    @Override
    public List<Event> findPendingEvents() {
        return eventRepository.findByType(Event.EventType.pending);
    }

    @Override
    @Transactional
    public String approveEvent(int event_id) {
        Event event = eventRepository.findByEventId(event_id);
        event.setType(Event.EventType.approved);
        eventRepository.save(event);
        emailService.eventApprovedEmail(event_id);
        return "approved successfully";
    }
    @Override
    @Transactional
    public String rejectEvent(int event_id) {
        Event event = eventRepository.findByEventId(event_id);
        event.setType(Event.EventType.rejected);
        eventRepository.save(event);
        emailService.eventRejectedEmail(event_id);
        return "rejected successfully";
    }
    @Override
    public List<Event> findEventsByQuery(EventQuery query) {
        return eventRepository.findByQuery(query);
    }

    @Override
    public Event findByEventId(int eventId) {
        return eventRepository.findByEventId(eventId);
    }
    @Override
    @Transactional
    public List<Event> recommend(List<Integer> appointedEvents, double[] usrImg) {
        List<Event> events = findEventsAfterCurrentDate();
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (!appointedEvents.contains(event.getEventId())) {
                filteredEvents.add(event);
            }
        }
        Comparator<Object> scoreComparator = Comparator.comparingDouble(event -> calculateSimilarity((Event) event, usrImg)).reversed();
        filteredEvents.sort(scoreComparator);
        return filteredEvents;
    }
    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage:参考余弦相似度计算
     */
    private double calculateSimilarity(Event event, double[] usrImg) {
        double[] feature = new double[17];
        featureVector(event, feature);
        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (int i = 0; i < feature.length; i++) {
            dotProduct += usrImg[i] * feature[i];
            magnitude1 += Math.pow(usrImg[i], 2);
            magnitude2 += Math.pow(feature[i], 2);
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        return dotProduct / (magnitude1 * magnitude2);
    }
    @Override
    public void featureVector(Event event, double[] feature) {
        feature[event.getCategoryId()-1]++;
        feature[event.getVenueId()+5]++;
        Date startTime = event.getStartTime();
        Date endTime = event.getEndTime();
        feature[15] += (double) (3600 * startTime.getHours() + 60 * startTime.getMinutes() + startTime.getSeconds())/86400;
        feature[16] += (double) (3600 * endTime.getHours() + 60 * endTime.getMinutes() + endTime.getSeconds())/86400;
    }
}

