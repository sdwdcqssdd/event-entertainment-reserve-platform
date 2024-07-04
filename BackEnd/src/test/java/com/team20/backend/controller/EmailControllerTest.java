package com.team20.backend.controller;


import com.team20.backend.model.Email;
import com.team20.backend.model.user.Users;
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
public class EmailControllerTest {

    @Mock
    private UsersService usersService;

    @Mock
    private EmailService emailService;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private EmailController emailController;

//    @Test
//    public void getEmailsForUserWithValidToken() {
//        Users user = new Users();
//        user.setUserId(1);
//        List<Email> emails = new ArrayList<>();
//        Email e = new Email();
//        e.setStatus(Email.EmailStatus.unread);
//        when(emailService.getEmailsByUserId(1)).thenReturn(emails);
//        String token = "token";
//        when(usersService.findUserByUsername(jwtUtils.getUsername(token))).thenReturn(user);
//        ResponseEntity<List<Email>> response = emailController.getEmailsForUser(token);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//    @Test
//    public void getEmailsForUserWithInvalidToken() {
//        String token = "token";
//        ResponseEntity<List<Email>> response = emailController.getEmailsForUser(token);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//    }
    @Test
    public void markEmailAsRead() {
        ResponseEntity<Void> response = emailController.markEmailAsRead(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    public void markEmailAsDelete() {
        ResponseEntity<Void> response = emailController.markEmailAsDeleted(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}