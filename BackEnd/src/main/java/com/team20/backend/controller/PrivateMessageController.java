package com.team20.backend.controller;


import com.team20.backend.dto.UserDTO;
import com.team20.backend.dto.UserDTOWithRead;
import com.team20.backend.model.ChatMessage;
import com.team20.backend.repository.ChatMessageRepository;
import com.team20.backend.service.DTOService;
import com.team20.backend.service.EmailService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PrivateMessageController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private DTOService dtoService;
    @Resource
    private JwtUtils jwtUtils;
    @PostMapping("/chatMessage")
    public ResponseEntity<String> PrivateChat(@RequestHeader("Authorization") String token, @RequestBody ChatMessage chatMessage) {
        if(!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setTime(LocalDateTime.now());
        emailService.PrivateChatMessage(chatMessage);
        chatMessageRepository.save(chatMessage);
        return ResponseEntity.ok("发送成功");
    }

    @GetMapping("/chatMessage")
    public ResponseEntity<List<ChatMessage>> getPrivateChat(@RequestHeader("Authorization") String token, @RequestParam String username) {
        if(!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<ChatMessage> chatMessages = chatMessageRepository.findByReceiverAndSender(jwtUtils.getUsername(token), username);
        for(ChatMessage chatMessage : chatMessages) chatMessage.setType(ChatMessage.MessageType.READ);
        chatMessageRepository.saveAll(chatMessages);
        List<ChatMessage> chatMessages1 = chatMessageRepository.findByReceiverAndSender(username, jwtUtils.getUsername(token));
        //将这两个list合并并按照时间排序
        chatMessages.addAll(chatMessages1);
        chatMessages.sort(Comparator.comparing(ChatMessage::getTime));
        return ResponseEntity.ok(chatMessages);
    }

    @GetMapping("/isRead")
    public ResponseEntity<Boolean> getReadStatus(@RequestHeader("Authorization") String token) {
        if(!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = jwtUtils.getUsername(token);
        List<ChatMessage> chatMessages = chatMessageRepository.findByReceiverAndType(username, ChatMessage.MessageType.CHAT);
        if(chatMessages.size() == 0) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/chatList")
    public ResponseEntity<List<UserDTOWithRead>> markAsRead(@RequestHeader("Authorization") String token) {
        if(!jwtUtils.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = jwtUtils.getUsername(token);
        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderOrReceiver(username, username);
        List<Map<String, Boolean>> res = new ArrayList<>();
        Map<String, Boolean> mp = new HashMap<>();
        for(ChatMessage chatMessage : chatMessages) {
            if(chatMessage.getSender().equals(username)) {
                //UserDTO userDTO = dtoService.getUserDTO(usersService.findUserByUsername(chatMessage.getReceiver()).getUserId());
                if(mp.containsKey(chatMessage.getReceiver())) {
                    mp.put(chatMessage.getReceiver(), chatMessage.getType() == ChatMessage.MessageType.READ);

                    res.add(Map.of(chatMessage.getReceiver(), chatMessage.getType() == ChatMessage.MessageType.READ));
                    continue;
                } else {
                    mp.put(chatMessage.getReceiver(), chatMessage.getType() == ChatMessage.MessageType.READ);
                }

                res.add(Map.of(chatMessage.getReceiver(), chatMessage.getType() == ChatMessage.MessageType.READ));
            } else {
                //UserDTO userDTO = dtoService.getUserDTO(usersService.findUserByUsername(chatMessage.getSender()).getUserId());
                if(mp.containsKey(chatMessage.getSender())) {
                    mp.put(chatMessage.getSender(), chatMessage.getType() == ChatMessage.MessageType.READ);
                    res.add(Map.of(chatMessage.getSender(), chatMessage.getType() == ChatMessage.MessageType.READ));
                    continue;
                } else {
                    mp.put(chatMessage.getSender(), chatMessage.getType() == ChatMessage.MessageType.READ);
                }
                res.add(Map.of(chatMessage.getSender(), chatMessage.getType() == ChatMessage.MessageType.READ));
            }
        }
        List<UserDTOWithRead> res1 = new ArrayList<>();
        for(Map<String, Boolean> map : res) {
            String key = map.keySet().iterator().next();
            UserDTO userDTO = dtoService.getUserDTO(usersService.findUserByUsername(key).getUserId());
            res1.add(new UserDTOWithRead(userDTO, map.get(key)));
        }
        return ResponseEntity.ok(res1);
    }
}
