package com.team20.backend.controller;

import com.team20.backend.dto.UserDTO;
import com.team20.backend.model.user.Follow;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.DTOService;
import com.team20.backend.service.FollowService;
import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.team20.backend.service.UsersService;

import java.util.List;


@Controller
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private FollowService followService;
    @Autowired
    private DTOService dtoService;
    @Resource
    private JwtUtils jwtUtils;
    @GetMapping("/signup")
    public ResponseEntity<String> signupForm() {
        return ResponseEntity.ok().body("注册");
    }

    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage: 注册和登录返回的json格式参考ai生成
     * 注册
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @return 注册成功返回token，失败返回错误信息
     */
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> signUp(@RequestParam String username,
                                         @RequestParam String email,
                                         @RequestParam String password) {
        Users user = usersService.register(username, email, password);
        if (user != null) {
            String token = jwtUtils.generateToken(user);
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.badRequest().body("用户名或邮箱已存在");
        }
    }


    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回token，失败返回错误信息
     */
    @PostMapping("/signin")
    @Transactional
    public ResponseEntity<String> signIn(@RequestParam String username,
                                         @RequestParam String password) {
        Users user = usersService.login(username, password);
        //System.out.println("response: " + response);
        if (user != null) {
            String token = jwtUtils.generateToken(user);
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.badRequest().body("用户名或密码错误");
        }
    }

    /**
     * 获取用户信息
     * @param token token
     * @return 用户信息
     */
    @GetMapping("/user_info")
    @Transactional
    public ResponseEntity<Users> getUserInfo(@RequestParam String token) {
        Users user = usersService.findUserByUsername(jwtUtils.getUsername(token));
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    @GetMapping("/user_info_byId")
    @Transactional
    public ResponseEntity<Users> getUserInfoById(@RequestParam int userId) {
        Users user = usersService.findUserById(userId);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 关注用户
     * @param token token
     * @param followeeId 被关注者id
     * @return 关注结果
     */
    @PostMapping("/follow")
    @Transactional
    public ResponseEntity<String> follow(@RequestHeader("Authorization") String token, @RequestParam int followeeId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int followerId = usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        followService.follow(followerId, followeeId);

        return ResponseEntity.status(HttpStatus.OK).body("关注成功");
    }

    /**
     * 取消关注用户
     * @param token token
     * @param followeeId 被关注者id
     * @return 取消关注结果
     */
    @PostMapping("/unfollow")
    @Transactional
    public ResponseEntity<String> unfollow(@RequestHeader("Authorization") String token, @RequestParam int followeeId) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int followerId = usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        followService.unfollow(followerId, followeeId);

        return ResponseEntity.status(HttpStatus.OK).body("取消关注成功");
    }


    /**
     * 获取粉丝列表
     * @param token token
     * @return 粉丝列表
     */
    @GetMapping("/followers")
    public ResponseEntity<List<UserDTO>> getFollowers(@RequestHeader("Authorization") String token) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int userId = usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        List<Follow> followers = followService.getFollowers(userId);
        List<UserDTO> userDTOs = followers.stream().map(follow ->
            dtoService.getUserDTO(follow.getFollowerId())
        ).toList();
        return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
    }

    /**
     * 获取关注者列表
     * @param token token
     * @return 关注者列表
     */
    @GetMapping("/followees")
    public ResponseEntity<List<UserDTO>> getFollowees(@RequestHeader("Authorization") String token) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int userId = usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId();
        List<Follow> followees = followService.getFollowees(userId);
        List<UserDTO> userDTOs = followees.stream().map(follow ->
                dtoService.getUserDTO(follow.getFolloweeId())
        ).toList();
        return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
    }

    /**
     * 判断指定用户是否关注
     * @param token token
     * @param user_id 用户id
     * @return 是否关注
     */
    @GetMapping("/is_following")
    public ResponseEntity<Boolean> isFollowing(@RequestHeader("Authorization") String token, @RequestParam int user_id) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Follow follow = followService.getByFollowerIdAndFolloweeId(usersService.findUserByUsername(jwtUtils.getUsername(token)).getUserId(), user_id);
        return ResponseEntity.status(HttpStatus.OK).body(follow != null);
    }


}
