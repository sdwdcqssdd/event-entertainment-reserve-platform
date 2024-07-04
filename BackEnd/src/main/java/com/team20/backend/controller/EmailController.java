package com.team20.backend.controller;

import com.team20.backend.model.Email;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.EmailService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmailController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private EmailService emailService;
    @Resource
    private JwtUtils jwtUtils;
//    @GetMapping("/emails")
//    public ResponseEntity<List<Email>> getEmailsForUser(@RequestHeader("Authorization") String token) {
//        if(!jwtUtils.validateSuperUser(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        Users user = usersService.findUserByUsername(jwtUtils.getUsername(token));
//        if (user != null) {
//            int userId = user.getUserId();
//            List<Email> emails = emailService.getEmailsByUserId(userId);
//            List<Email> newEmails=new ArrayList<>();
//            for(Email e:emails){
//                if (e.getStatus()!= Email.EmailStatus.deleted) newEmails.add(e);
//            }
//            newEmails.sort((e1, e2) -> e2.getTime().compareTo(e1.getTime()));
//            return ResponseEntity.ok().body(newEmails);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }

    @GetMapping("/emails")
    public ResponseEntity<List<Email>> getEmailsForUser(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix and extract the user ID from the token
        String username = jwtUtils.getUsername(token.substring(7));
        Users users=usersService.findUserByUsername(username);
        int userId = users.getUserId();
        if (userId != 0) {
            List<Email> emails = emailService.getEmailsByUserId(userId);
            if (emails.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList()); // 返回一个空列表
            }
            List<Email> filteredEmails = emails.stream()
                    .filter(email -> email.getStatus() != Email.EmailStatus.deleted)
                    .sorted((e1, e2) -> e2.getTime().compareTo(e1.getTime()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredEmails);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/emails/mark-as-read")
    public ResponseEntity<Void> markEmailAsRead(@RequestParam int emailId) {
        emailService.markEmailAsRead(emailId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping("/emails/mark-as-deleted")
    public ResponseEntity<Void> markEmailAsDeleted(@RequestParam int emailId) {
        emailService.markEmailAsDeleted(emailId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
