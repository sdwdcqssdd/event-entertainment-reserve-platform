package com.team20.backend.controller;

import com.team20.backend.dto.MessageDTO;
import com.team20.backend.dto.UserDTO;
import com.team20.backend.model.ChatMessage;
import com.team20.backend.model.Email;
import com.team20.backend.repository.ChatMessageRepository;
import com.team20.backend.service.DTOService;
import com.team20.backend.service.EmailService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    EmailService emailService;

    @Autowired
    DTOService dtoService;

    @Autowired
    UsersService usersService;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDTO chatMessage) {
        chatMessage.setTime(LocalDateTime.now());
        logger.info("Received message: {}", chatMessage);

        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}