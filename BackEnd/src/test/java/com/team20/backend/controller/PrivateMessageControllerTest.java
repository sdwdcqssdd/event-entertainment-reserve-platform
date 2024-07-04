package com.team20.backend.controller;

import com.team20.backend.dto.UserDTO;
import com.team20.backend.dto.UserDTOWithRead;
import com.team20.backend.model.ChatMessage;
import com.team20.backend.model.user.Users;
import com.team20.backend.repository.ChatMessageRepository;
import com.team20.backend.service.DTOService;
import com.team20.backend.service.EmailService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PrivateMessageControllerTest {
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private UsersService usersService;
    @Mock
    private DTOService dtoService;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private PrivateMessageController privateMessageController;

    @Test
    public void PrivateChat() {
        String token = "token";
        ChatMessage chatMessage = new ChatMessage();
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = privateMessageController.PrivateChat(token,chatMessage);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = privateMessageController.PrivateChat(token,chatMessage);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getPrivateChat() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<ChatMessage>> response = privateMessageController.getPrivateChat(token,"name");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("username");
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = privateMessageController.getPrivateChat(token,"name");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getReadStatus() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<Boolean> response = privateMessageController.getReadStatus(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        List<ChatMessage> chatMessages = new ArrayList<>();
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(chatMessageRepository.findByReceiverAndType("name", ChatMessage.MessageType.CHAT)).thenReturn(chatMessages);
        response = privateMessageController.getReadStatus(token);
        assertThat(response.getBody()).isEqualTo(false);
        chatMessages.add(new ChatMessage());
        response = privateMessageController.getReadStatus(token);
        assertThat(response.getBody()).isEqualTo(true);
    }
    @Test
    public void markAsRead() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<UserDTOWithRead>> response = privateMessageController.markAsRead(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        List<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("name");
        chatMessage.setReceiver("alice");
        chatMessages.add(chatMessage);
        ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setSender("name");
        chatMessage2.setReceiver("alice");
        chatMessages.add(chatMessage2);
        ChatMessage chatMessage3 = new ChatMessage();
        chatMessage3.setSender("alice");
        chatMessages.add(chatMessage3);
        ChatMessage chatMessage4 = new ChatMessage();
        chatMessage4.setSender("bob");
        chatMessages.add(chatMessage4);
        when(chatMessageRepository.findBySenderOrReceiver("name", "name")).thenReturn(chatMessages);
        Users users = new Users();
        users.setUserId(1);
        users.setUsername("alice");
        users.setIdentity(Users.IdentityType.USER);
        Users users2 = new Users();
        users2.setUserId(2);
        users2.setUsername("bob");
        users2.setIdentity(Users.IdentityType.USER);
        byte[] bytes = new byte[2];
        when(usersService.findUserByUsername("alice")).thenReturn(users);
        when(usersService.findUserByUsername("bob")).thenReturn(users2);
        when(dtoService.getUserDTO(1)).thenReturn(new UserDTO(users,bytes));
        when(dtoService.getUserDTO(2)).thenReturn(new UserDTO(users2,bytes));
        response = privateMessageController.markAsRead(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
