package com.team20.backend.controller;

import com.team20.backend.dto.UserDTO;
import com.team20.backend.model.user.Follow;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.DTOService;
import com.team20.backend.service.FollowService;
import com.team20.backend.service.UsersService;
import com.team20.backend.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {
    @Mock
    private UsersService usersService;
    @Mock
    private FollowService followService;
    @Mock
    private DTOService dtoService;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private UsersController usersController;
    @Test
    public void signUp() {
        String username = "name";
        String email = "12110715@mail.sustech.edu.cn";
        String password = "1234";

        ResponseEntity<String> response = usersController.signUp(username,email,password);
        assertThat(response.getBody()).isEqualTo("用户名或邮箱已存在");
        when(usersService.register(username, email, password)).thenReturn(new Users());
        response = usersController.signUp(username,email,password);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void signIn() {
        String username = "name";
        String password = "1234";
        ResponseEntity<String> response = usersController.signIn(username,password);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        when(usersService.login(username, password)).thenReturn(new Users());
        response = usersController.signIn(username,password);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getUserInfo() {
        String token = "token";
        when(jwtUtils.getUsername(token)).thenReturn("name");
        when(usersService.findUserByUsername("name")).thenReturn(new Users());
        ResponseEntity<Users> response = usersController.getUserInfo(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        when(usersService.findUserByUsername("name")).thenReturn(null);
        response = usersController.getUserInfo(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    public void follow() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = usersController.follow(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(2);
        when(usersService.findUserByUsername("name")).thenReturn(users);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = usersController.follow(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void unfollow() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<String> response = usersController.unfollow(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(2);
        when(usersService.findUserByUsername("name")).thenReturn(users);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        response = usersController.unfollow(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getFollowers() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<UserDTO>> response = usersController.getFollowers(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(2);
        when(usersService.findUserByUsername("name")).thenReturn(users);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        List<Follow> followers = new ArrayList<>();
        Follow follow = new Follow();
        follow.setFollowerId(2);
        followers.add(follow);
        Users users1 = new Users();
        users1.setUserId(4);
        users1.setUsername("fl");
        users1.setIdentity(Users.IdentityType.USER);
        byte[] bytes = new byte[2];
        when(dtoService.getUserDTO(anyInt())).thenReturn(new UserDTO(users1,bytes));
        when(followService.getFollowers(2)).thenReturn(followers);
        response = usersController.getFollowers(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void getFollowees() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<List<UserDTO>> response = usersController.getFollowees(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(2);
        when(usersService.findUserByUsername("name")).thenReturn(users);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        List<Follow> followees = new ArrayList<>();
        Follow follow = new Follow();
        follow.setFolloweeId(2);
        followees.add(follow);
        Users users1 = new Users();
        users1.setUserId(4);
        users1.setUsername("fe");
        users1.setIdentity(Users.IdentityType.USER);
        byte[] bytes = new byte[2];
        when(dtoService.getUserDTO(anyInt())).thenReturn(new UserDTO(users1,bytes));
        when(followService.getFollowees(2)).thenReturn(followees);
        response = usersController.getFollowees(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void isFollowing() {
        String token = "token";
        when(jwtUtils.validateToken(token)).thenReturn(false);
        ResponseEntity<Boolean> response = usersController.isFollowing(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        when(jwtUtils.getUsername(token)).thenReturn("name");
        Users users = new Users();
        users.setUserId(2);
        when(usersService.findUserByUsername("name")).thenReturn(users);
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(followService.getByFollowerIdAndFolloweeId(2, 1)).thenReturn(new Follow());
        response = usersController.isFollowing(token,1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
