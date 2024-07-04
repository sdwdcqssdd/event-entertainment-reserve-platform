package com.team20.backend.controller;

import com.team20.backend.dto.EventDTO;
import com.team20.backend.dto.EventQuery;
import com.team20.backend.dto.UserEventDTO;
import com.team20.backend.model.*;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.*;
import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EventController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private EventService eventService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private DTOService dtoService;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * 推荐活动
     * @param token token
     * @return 推荐活动
     */
    @GetMapping ("/recommend")
    @Transactional
    public ResponseEntity<List<EventDTO>> getRecommendEvent(@RequestHeader("Authorization") String token) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtUtils.getUsername(token);
        List<Integer> appointedEvents = new ArrayList<>();
        List<UserEvent> appoints = userEventService.findUserEventByUserId(usersService.findUserByUsername(username).getUserId());
        if (appoints.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(eventService.findEventsAfterCurrentDate().stream()
                    .map(dtoService::convertToDTO).toList());
        }
        double[] usrImg = new double[17];
        int cnt = 0;
        for (UserEvent userEvent: appoints) {
            Event event = eventService.findByEventId(userEvent.getEventId());
            eventService.featureVector(event, usrImg);
            appointedEvents.add(event.getEventId());
            cnt++;
        }
        for (int i = 0; i < usrImg.length; i++) {
            usrImg[i] /= cnt;
        }
        List<Event> recommend = eventService.recommend(appointedEvents, usrImg);
        List<EventDTO> eventDTOS = recommend.stream()
                .map(dtoService::convertToDTO).toList();
        return ResponseEntity.ok().body(eventDTOS);
    }


    @PostMapping("/events_by_query")
    @Transactional
    public ResponseEntity<List<EventDTO>> getEvents(@RequestBody EventQuery query) {
        if (query.getOrganizerNames() != null) {
            List<String> organizerNames = query.getOrganizerNames();
            List<Integer> organizerIds = new ArrayList<>();
            for (String name : organizerNames) {
                Users user = usersService.findUserByUsername(name);
                if(user != null) {
                    organizerIds.add(user.getUserId());
                }
            }
            query.setOrganizerIds(organizerIds);
        }

        if (query.getVenueNames() != null) {
            List<String> venueNames = query.getVenueNames();
            List<Integer> venueIds = new ArrayList<>();
            for (String name : venueNames) {
                Venue venue = venueService.findByName(name);
                if (venue != null) {
                    venueIds.add(venue.getVenueId());
                }
            }
            //System.out.println(venueIds);
            query.setVenueIds(venueIds);
        }
        List<Event> events = eventService.findEventsByQuery(query);
        Date today = new Date();
        List<Event> filteredEvents = events.stream()
                .filter(event -> event.getDate().after(today) && !"approved".equals(event.getType().toString())).toList();
        List<EventDTO> eventDTOS = filteredEvents.stream()
                .map(dtoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(eventDTOS);
    }

    /**
     * 获取指定ID活动
     * @param eventId 活动ID
     * @return 活动
     */
    @GetMapping("/events/{eventId}")
    @Transactional
    public ResponseEntity<EventDTO> getEventById(@PathVariable int eventId) {
        Event event = eventService.findByEventId(eventId);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(dtoService.convertToDTO(event));
    }

    /**
     * 获取当前所有活动
     * @return 活动列表
     */
    @GetMapping("/events")
    @Transactional
    public ResponseEntity<List<EventDTO>> getEventsAfterCurrentDate() {
        List<Event> events = eventService.findEventsAfterCurrentDate();
        return ResponseEntity.status(HttpStatus.OK).body(events.stream()
                .map(dtoService::convertToDTO).toList());
    }


    /**
     * 超级用户获取待审核活动
     * @param token token
     * @return 待审核活动
     */
    @GetMapping("/superuser/events")
    @Transactional
    public ResponseEntity<List<Event>> getPendingEvents(@RequestHeader("Authorization") String token) {
        if (jwtUtils.validateSuperUser(token)) {
            List<Event> events = eventService.findPendingEvents();
            return ResponseEntity.ok().body(events);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 超级用户审核活动
     * @param token token
     * @param event_id 活动id
     * @return 审核结果
     */
    @PostMapping("/superuser/events/approve")
    @Transactional
    public ResponseEntity<String> approveEvent(@RequestHeader("Authorization") String token, @RequestParam("eventId") int event_id) {
        if (jwtUtils.validateSuperUser(token)) {
            return ResponseEntity.ok().body(eventService.approveEvent(event_id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 超级用户拒绝活动
     * @param token token
     * @param event_id 活动id
     * @return 拒绝结果
     */
    @PostMapping( "/superuser/events/reject")
    @Transactional
    public ResponseEntity<String> rejectEvent(@RequestHeader("Authorization") String token, @RequestParam("eventId") int event_id) {
        if (jwtUtils.validateSuperUser(token)) {
            return ResponseEntity.ok().body(eventService.rejectEvent(event_id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 超级用户或者活动组织者获取待审核活动
     * @param token token
     * @return  待审核活动
     */
    @PostMapping("/Appoint")
    @Transactional
    public ResponseEntity<List<UserEvent>> getAppoint(@RequestHeader("Authorization") String token, @RequestParam("eventId") int eventId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtUtils.getUsername(token);
        if(jwtUtils.validateSuperUser(token) || usersService.findUserByUsername(username).getUserId() == eventService.findByEventId(eventId).getOrganizerId()) {
            List<UserEvent> userEvents = userEventService.findUserEventByStatusAndEventId("pending", eventId);
            return ResponseEntity.ok().body(userEvents);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 超级用户或者活动组织者批准预约
     * @param token token
     * @param user_event_id 预约id
     * @return 批准结果
     */
    @PostMapping("/Appoint/approve")
    @Transactional
    public ResponseEntity<String> approveAppoint(@RequestHeader("Authorization") String token, @RequestParam("appointId") int user_event_id) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtUtils.getUsername(token);
        if(jwtUtils.validateSuperUser(token) || usersService.findUserByUsername(username).getUserId() == userEventService.getEventOrganizerByUserEventId(user_event_id)) {
            return ResponseEntity.ok().body(userEventService.approveAppoint(user_event_id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 超级用户或者活动组织者拒绝预约
     * @param token token
     * @param user_event_id 预约id
     * @return 拒绝结果
     */
    @PostMapping("/Appoint/reject")
    @Transactional
    public ResponseEntity<String> rejectAppoint(@RequestHeader("Authorization") String token, @RequestParam("appointId") int user_event_id) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtUtils.getUsername(token);
        if(jwtUtils.validateSuperUser(token) || usersService.findUserByUsername(username).getUserId() == userEventService.getEventOrganizerByUserEventId(user_event_id)) {
            return ResponseEntity.ok().body(userEventService.rejectAppoint(user_event_id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /**
     * 返回指定日期的活动
     * @param dateString 日期
     * @return 活动列表
     * @throws ParseException 日期解析异常
     */
    @GetMapping("/events/by-date")
    @Transactional
    public ResponseEntity<List<EventDTO>> getEventsOnDate(@RequestParam("date") String dateString) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        List<Event> events = eventService.findEventsOnDate(date);
        return ResponseEntity.ok().body(events.stream()
                .map(dtoService::convertToDTO).toList());
    }

    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage: 用来给出范例，学习了相关语法，并根据需要重写了validateEvent
     * 创建活动
     * @param token token
     * @param event 活动
     * @return 创建结果
     */
    @PostMapping("/events")
    @Transactional
    public ResponseEntity<Map<String, String>> createEvent(@RequestHeader("Authorization") String token, @RequestBody Event event) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> response = new HashMap<>();
        String error = validateEvent(event);
        event.setOrganizerId(usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId());
        System.out.println(event.getOrganizerId());
        if (error != null) {
            response.put("message", error);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        eventService.saveEvent(event);
        response.put("message", "活动创建成功，请等待审核");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    /**
     * 用于验证一个活动是否合规
     * @param event 活动
     * @return 错误信息
     */
    private String validateEvent(Event event) {
        if(event.getTitle() == null || event.getTitle().isEmpty()) return "需要标题";
        if(event.getDate() == null) return "需要日期";
        if(event.getStartTime() == null) return "需要起始时间";
        if(event.getEndTime() == null) return "需要结束时间";
        event.setType(Event.EventType.pending);
        Venue venue = venueService.findByVenueId(event.getVenueId());
        if(venue == null) return "无效区域";
        if(event.getCapacityLimit() == null) {
            event.setCapacityLimit(venue.getCapacity());
            event.setRemaining(venue.getCapacity() < event.getCapacityLimit() ? venue.getCapacity() : event.getCapacityLimit());
        } else if(event.getCapacityLimit() > venue.getCapacity()) {
            return "容量超限";
        } else event.setRemaining(event.getCapacityLimit());
        return null;
    }

    /**
     * 预约活动
     * @param token token
     * @param eventId 活动id
     * @return 预约结果
     */
    @PostMapping("/events/appoint")
    @Transactional
    public ResponseEntity<Map<String, String>> appoint(@RequestHeader("Authorization") String token, @RequestParam("eventId") int eventId) {
        userEventService.reserveEvent(usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId(), eventId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "预约成功");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 登录用户获取历史活动(自己的历史记录)
     * @param token token
     * @return 历史活动
     */
    @PostMapping("/userAppoint")
    @Transactional
    public ResponseEntity<List<UserEventDTO>> getUserHistoryEvents(@RequestHeader("Authorization") String token) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtUtils.getUsername(token);
        List<UserEvent> appoints = userEventService.findUserEventByUserId(usersService.findUserByUsername(username).getUserId());
        List<UserEventDTO> appointDTO = appoints.stream().map(userEvent ->
                new UserEventDTO(userEvent, dtoService.getEventDTO(userEvent.getEventId()))).collect(Collectors.toList());
        return ResponseEntity.ok().body(appointDTO);
    }
}

