package com.team20.backend.model.user;

import jakarta.persistence.*;

@Entity
public class Users {
    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage: Column注释参考gpt生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "identity", length = 20)
    @Enumerated(EnumType.STRING)
    private IdentityType identity;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public IdentityType getIdentity() {
        return identity;
    }

    public void setIdentity(IdentityType identity) {
        this.identity = identity;
    }
    /**
     * AI-generated-content
     * tool: chatGpt
     * version: 3.5
     * usage: 枚举类参考gpt生成
     */

    public enum IdentityType {
        USER,
        SUPERUSER
    }
}
