package com.team20.backend.controller;

import com.team20.backend.controller.ChatController;
import com.team20.backend.dto.MessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class ChatControllerTest {


    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        chatController = new ChatController(messagingTemplate);
    }
    @Test
    public void testSendMessage() {
        MessageDTO messageDTO = new MessageDTO("Alice","Hello",LocalDateTime.now());
        chatController.sendMessage(messageDTO);
        verify(messagingTemplate, times(1)).convertAndSend("/topic/public", messageDTO);
    }
    
}