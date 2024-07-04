package com.team20.backend.controller;


import com.team20.backend.model.user.Avatar;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.AvatarService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class AvatarControllerTest {

    @Mock
    private AvatarService avatarService;

    @Mock
    private UsersService usersService;
    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AvatarController avatarController;

    @Test
    public void getAvatarInvalid() {
        Avatar defaultAvatar = new Avatar();
        when(avatarService.getDefaultAvatar()).thenReturn(defaultAvatar);
        String invalidToken = "invalidToken";
        when(jwtUtils.validateToken(invalidToken)).thenReturn(false);
        ResponseEntity<Avatar> response = avatarController.getAvatar(invalidToken);
        assertThat(response.getBody()).isEqualTo(defaultAvatar);
    }

    @Test
    public void getAvatarValid() {
        Avatar defaultAvatar = new Avatar();
        when(avatarService.getDefaultAvatar()).thenReturn(defaultAvatar);
        String validToken = "validToken";
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.getUsername(validToken)).thenReturn("username");
        Users users = new Users();
        users.setUserId(1);
        users.setUsername("username");
        when(usersService.findUserByUsername("username")).thenReturn(users);
        List<Avatar> avatars = new ArrayList<>();
        Avatar a = new Avatar();
        a.setStatus("approved");
        avatars.add(a);
        when(avatarService.findAvatarByUserId(users.getUserId())).thenReturn(avatars);
        ResponseEntity<Avatar> response = avatarController.getAvatar(validToken);
        assertThat(response.getBody()).isEqualTo(a);
    }

    @Test
    public void getAvatarWithNoUser() {
        Avatar defaultAvatar = new Avatar();
        when(avatarService.getDefaultAvatar()).thenReturn(defaultAvatar);
        String validToken = "validToken";
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.getUsername(validToken)).thenReturn("username");
        when(usersService.findUserByUsername("username")).thenReturn(null);
        ResponseEntity<Avatar> response = avatarController.getAvatar(validToken);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAvatarDefault() {
        Avatar defaultAvatar = new Avatar();
        when(avatarService.getDefaultAvatar()).thenReturn(defaultAvatar);
        String validToken = "validToken";
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.getUsername(validToken)).thenReturn("username");
        Users users = new Users();
        users.setUserId(1);
        users.setUsername("username");
        when(usersService.findUserByUsername("username")).thenReturn(users);
        List<Avatar> avatars = new ArrayList<>();
        Avatar a = new Avatar();
        a.setStatus("pending");
        avatars.add(a);
        when(avatarService.findAvatarByUserId(users.getUserId())).thenReturn(avatars);
        ResponseEntity<Avatar> response = avatarController.getAvatar(validToken);
        assertThat(response.getBody()).isEqualTo(defaultAvatar);
    }

    @Test
    public void uploadAvatarWithInvalidToken() {
        String invalidToken = "invalidToken";
        Map<String, String> requestBody = new HashMap<>();
        when(jwtUtils.validateToken(invalidToken)).thenReturn(false);
        ResponseEntity<String> response = avatarController.uploadAvatar(invalidToken, requestBody);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void uploadAvatarWithValidToken() {
        String validToken = "validToken";
        Map<String, String> requestBody = new HashMap<>();
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        ResponseEntity<String> response = avatarController.uploadAvatar(validToken, requestBody);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void uploadAvatarWithValidTokenAndData() {
        String validToken = "validToken";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("avatarData","base64AvatarData");
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.getUsername(validToken)).thenReturn("username");
        when(usersService.findUserByUsername("username")).thenReturn(null);
        ResponseEntity<String> response = avatarController.uploadAvatar(validToken, requestBody);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void uploadAvatarWithValidTokenAndDataAndUser() {
        String validToken = "validToken";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("avatarData","base64AvatarData");
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.getUsername(validToken)).thenReturn("username");
        Users users = new Users();
        users.setUsername("username");
        users.setUserId(1);
        when(usersService.findUserByUsername("username")).thenReturn(users);
        List<Avatar> avatars = new ArrayList<>();
        when(avatarService.findAvatarByUserId(users.getUserId())).thenReturn(avatars);
        ResponseEntity<String> response = avatarController.uploadAvatar(validToken, requestBody);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void uploadAvatarWithValidTokenAndDataAndUserAndAvatar() {
        String validToken = "validToken";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("avatarData","base64AvatarData");
        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.getUsername(validToken)).thenReturn("username");
        Users users = new Users();
        users.setUsername("username");
        users.setUserId(1);
        when(usersService.findUserByUsername("username")).thenReturn(users);
        List<Avatar> avatars = new ArrayList<>();
        Avatar avatar = new Avatar();
        Avatar avatar1 = new Avatar();
        avatars.add(avatar);
        avatars.add(avatar1);
        when(avatarService.findAvatarByUserId(users.getUserId())).thenReturn(avatars);
        ResponseEntity<String> response = avatarController.uploadAvatar(validToken, requestBody);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllPendingAvatarsWithInvalidToken() {
        String invalidToken = "invalidToken";
        when(jwtUtils.validateSuperUser(invalidToken)).thenReturn(false);
        ResponseEntity<List<Avatar>> response = avatarController.getAllPendingAvatars(invalidToken);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getAllPendingAvatarsWithValidToken() {
        String validToken = "validToken";
        List<Avatar> pendingAvatars = new ArrayList<>();
        when(jwtUtils.validateSuperUser(validToken)).thenReturn(true);
        when(avatarService.findByStatus("pending")).thenReturn(pendingAvatars);
        ResponseEntity<List<Avatar>> response = avatarController.getAllPendingAvatars(validToken);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void approveAvatarWithInvalidToken() {
        String invalidToken = "invalidToken";
        when(jwtUtils.validateSuperUser(invalidToken)).thenReturn(false);
        ResponseEntity<String> response = avatarController.approveAvatar(invalidToken,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void approveAvatarWithValidToken() {
        String validToken = "validToken";
        when(jwtUtils.validateSuperUser(validToken)).thenReturn(true);
        ResponseEntity<String> response = avatarController.approveAvatar(validToken,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Avatar avatar = new Avatar();
        avatar.setStatus("approved");
        when(avatarService.findAvatarById(2)).thenReturn(avatar);
        response = avatarController.approveAvatar(validToken,2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void approveAvatarWithValidTokenAndAvatar() {
        String validToken = "validToken";
        when(jwtUtils.validateSuperUser(validToken)).thenReturn(true);
        Avatar avatar = new Avatar();
        avatar.setAvatarId(1);
        avatar.setUserId(1);
        avatar.setStatus("pending");
        when(avatarService.findAvatarById(1)).thenReturn(avatar);
        List<Avatar> avatars = new ArrayList<>();
        avatars.add(avatar);
        when(avatarService.findAvatarByUserId(avatar.getUserId())).thenReturn(avatars);
        ResponseEntity<String> response = avatarController.approveAvatar(validToken,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void rejectAvatarWithInvalidToken() {
        String invalidToken = "invalidToken";
        when(jwtUtils.validateSuperUser(invalidToken)).thenReturn(false);
        ResponseEntity<String> response = avatarController.rejectAvatar(invalidToken,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void rejectAvatarWithValidToken() {
        String validToken = "validToken";
        when(jwtUtils.validateSuperUser(validToken)).thenReturn(true);
        ResponseEntity<String> response = avatarController.rejectAvatar(validToken,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Avatar avatar = new Avatar();
        avatar.setStatus("approved");
        when(avatarService.findAvatarById(2)).thenReturn(avatar);
        response = avatarController.rejectAvatar(validToken,2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void rejectAvatarWithValidTokenAndAvatar() {
        String validToken = "validToken";
        when(jwtUtils.validateSuperUser(validToken)).thenReturn(true);
        Avatar avatar = new Avatar();
        avatar.setAvatarId(1);
        avatar.setUserId(1);
        avatar.setStatus("pending");
        when(avatarService.findAvatarById(1)).thenReturn(avatar);
        ResponseEntity<String> response = avatarController.rejectAvatar(validToken,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}