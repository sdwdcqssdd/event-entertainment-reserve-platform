package com.team20.backend.controller;

import com.team20.backend.dto.*;
import com.team20.backend.model.*;
import com.team20.backend.model.history.Comment;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HistoryEventController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private EventService eventService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VenueService venueService;
    @Autowired
    private AvatarService avatarService;
    @Autowired
    private DTOService dtoService;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * 获取指定活动信息
     * @param eventId 活动id
     * @return 活动信息
     */
    @GetMapping("/historyevents/{eventId}")
    @Transactional
    public ResponseEntity<EventCommentDTO> getHistoryEventById(@PathVariable int eventId) {
        return ResponseEntity.ok().body(dtoService.getEventCommentDTO(eventId));
    }

    /**
     * 获取历史活动
     * @param query 查询条件
     * @return 活动列表
     */
    @PostMapping("/historyevents")
    @Transactional
    public ResponseEntity<List<EventDTO>> getHistoryEvents(@RequestBody EventQuery query) {
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
            System.out.println(venueIds);
            query.setVenueIds(venueIds);
        }
        List<Event> events = eventService.findEventsByQuery(query);
        Date today = new Date();
        List<Event> filteredEvents = events.stream()
                .filter(event -> !event.getDate().after(today) && !"approved".equals(event.getType().toString())).toList();
        List<EventDTO> eventDTOS = filteredEvents.stream()
                .map(dtoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(eventDTOS);
    }

    /**
     * 发起评分
     * @param token token
     * @param eventId 活动id
     * @param content 评价内容
     * @param rating 评分
     * @return 评分
     */
    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestHeader("Authorization") String token,
                                        @RequestParam int eventId,
                                        @RequestParam String content,
                                        @RequestParam int rating) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }
        try {
            int userId = usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
            Comment comment = commentService.addComment(userId, eventId, content, rating);
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * 修改评分
     * @param token token
     * @param eventId 活动id
     * @param content 评价内容
     * @param rating 评分
     * @return 评分
     */
    @PutMapping("/comments")
    public ResponseEntity<?> modifyComment(@RequestHeader("Authorization") String token,
                                                 @RequestParam int eventId,
                                                 @RequestParam String content,
                                                 @RequestParam int rating) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int userId = usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        try {
            Comment comment = commentService.modifyComment(userId, eventId, content, rating);
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * 删除评分
     * @param token token
     * @param eventId 活动id
     * @return 评分
     */
    @DeleteMapping("/comments")
    public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String token,
                                              @RequestParam int eventId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int userId = usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        try {
            commentService.deleteComment(userId, eventId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
