package com.team20.backend.model.user;

import jakarta.persistence.*;

@Entity
@Table(name = "Avatars")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id")
    private int avatarId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "avatar_data")
    private byte[] avatarData;

    @Column(name = "status", nullable = false)
    private String status;

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(byte[] avatarData) {
        this.avatarData = avatarData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
