package com.team20.backend.dto;

import com.team20.backend.model.user.Users;

public class UserDTO {
    private int userId;
    private String userName;
    private byte[] avatarData;
    private String identity;

    public UserDTO(Users user, byte[] avatarData) {
        this.userId = user.getUserId();
        this.userName = user.getUsername();
        this.avatarData = avatarData;
        this.identity = user.getIdentity().toString();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(byte[] avatarData) {
        this.avatarData = avatarData;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
