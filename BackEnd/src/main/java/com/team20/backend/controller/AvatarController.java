package com.team20.backend.controller;

import com.team20.backend.model.user.Avatar;
import com.team20.backend.model.user.Users;
import com.team20.backend.service.*;
import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AvatarController {
    @Autowired
    private AvatarService avatarService;

    @Autowired
    private UsersService usersService;
    @Resource
    private JwtUtils jwtUtils;
    /**
     * 获取头像
     * @param token token
     * @return 头像
     */
    @GetMapping("/avatars")
    @Transactional
    public ResponseEntity<Avatar> getAvatar(@RequestHeader("Authorization") String token) {
        Avatar defaultAvatar = avatarService.getDefaultAvatar();
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(defaultAvatar);
        }
        String username = jwtUtils.getUsername(token);
        Users user = usersService.findUserByUsername(username);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Avatar> avatar = avatarService.findAvatarByUserId(user.getUserId());
        //找到avatar中approved的
        for (Avatar a : avatar) {
            if (a.getStatus().equals("approved")) {
                return ResponseEntity.status(HttpStatus.OK).body(a);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(defaultAvatar);
    }

    /**
     * 上传头像
     * @param token token
     * @param requestBody 请求体
     * @return 上传结果
     */
    @PostMapping("/avatars")
    @Transactional
    public ResponseEntity<String> uploadAvatar(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> requestBody) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String base64AvatarData = requestBody.get("avatarData");
        if (base64AvatarData == null || base64AvatarData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Avatar data is missing");
        }

        byte[] avatarData = Base64.decodeBase64(base64AvatarData);

        String username = jwtUtils.getUsername(token);
        Users user = usersService.findUserByUsername(username);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Avatar> avatars = avatarService.findAvatarByUserId(user.getUserId()); // 检查是否已经上传过头像
        if(avatars.size() > 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Avatar already exists");
        }
        Avatar avatar = new Avatar();
        avatar.setUserId(user.getUserId());
        avatar.setAvatarData(avatarData);
        avatar.setStatus("pending"); // 设置为待审核状态

        avatarService.saveAvatar(avatar);

        return ResponseEntity.status(HttpStatus.CREATED).body("Avatar uploaded successfully. Please wait for approval.");
    }

    /**
     * 超级用户获取待审核头像
     * @param token token
     * @return 待审核头像
     */
    @GetMapping("/avatars/pending")
    @Transactional
    public ResponseEntity<List<Avatar>> getAllPendingAvatars(@RequestHeader("Authorization") String token) {
        if (!jwtUtils.validateSuperUser(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Avatar> pendingAvatars = avatarService.findByStatus("pending");
        return ResponseEntity.status(HttpStatus.OK).body(pendingAvatars);
    }

    /**
     * 超级用户批准头像
     * @param token token
     * @param avatarId 头像id
     * @return 批准结果
     */
    @PostMapping("/avatars/approve")
    @Transactional
    public ResponseEntity<String> approveAvatar(@RequestHeader("Authorization") String token, @RequestParam int avatarId) {
        if (!jwtUtils.validateSuperUser(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Avatar avatar = avatarService.findAvatarById(avatarId);
        if (avatar == null || !avatar.getStatus().equals("pending")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found or not pending");
        }
        List<Avatar> avatars = avatarService.findAvatarByUserId(avatar.getUserId());
        for (Avatar a : avatars) {
            if (a.getStatus().equals("approved")) {
                avatarService.deleteAvatar(a.getAvatarId());
            }
        }

        avatar.setStatus("approved");
        avatarService.saveAvatar(avatar);

        return ResponseEntity.status(HttpStatus.OK).body("Avatar approved successfully");
    }

    /**
     * 超级用户拒绝头像
     * @param token token
     * @param avatarId 头像id
     * @return 拒绝结果
     */
    @PostMapping("/avatars/reject")
    @Transactional
    public ResponseEntity<String> rejectAvatar(@RequestHeader("Authorization") String token, @RequestParam int avatarId) {
        if (!jwtUtils.validateSuperUser(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Avatar avatar = avatarService.findAvatarById(avatarId);
        if (avatar == null || !avatar.getStatus().equals("pending")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found or not pending");
        }

        avatarService.deleteAvatar(avatarId); // 删除头像

        return ResponseEntity.status(HttpStatus.OK).body("Avatar rejected and deleted successfully");
    }

}
