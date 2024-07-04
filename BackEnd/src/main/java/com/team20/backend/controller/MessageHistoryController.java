package com.team20.backend.controller;

import com.team20.backend.model.ChatMessage;
import com.team20.backend.repository.ChatMessageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

//TODO: 是否需要这个？ 需要前端验证
@RestController
public class MessageHistoryController {

    private final ChatMessageRepository chatMessageRepository;

    public MessageHistoryController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/messages/history")
    public List<ChatMessage> getMessageHistory(@RequestParam String username) {
        // Retrieve messages where the user is either the sender or receiver
        return chatMessageRepository.findBySenderOrReceiver(username, username);
    }
}

