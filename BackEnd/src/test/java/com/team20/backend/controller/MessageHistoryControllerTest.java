package com.team20.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.team20.backend.model.ChatMessage;
import com.team20.backend.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;

public class MessageHistoryControllerTest {

    private MessageHistoryController messageHistoryController;
    @Mock
    private ChatMessageRepository chatMessageRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        messageHistoryController = new MessageHistoryController(chatMessageRepository);
    }

    @Test
    public void testGetMessageHistory() {
        List<ChatMessage> result = new ArrayList<>();
        when(chatMessageRepository.findBySenderOrReceiver("name","name")).thenReturn(result);
        List<ChatMessage> res = messageHistoryController.getMessageHistory("name");
        assertThat(res).isEqualTo(result);
    }
}