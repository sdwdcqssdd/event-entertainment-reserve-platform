package com.team20.backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Email model class corresponding to the Emails table in the database.
 * Provides getters and setters for managing Email objects.
 */
@Entity
@Table(name = "Emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "UserID", nullable = false)
    private int userID;

    @Column(name = "Subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "Body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "Time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp time;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    // Default constructor
    public Email() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public enum EmailStatus {
        unread,
        read,
        deleted
    }
//    @Override
//    public String toString() {
//        return "Email{" +
//                "id=" + id +
//                ", userID=" + userID +
//                ", subject='" + subject + '\'' +
//                ", body='" + body + '\'' +
//                ", time=" + time +
//                '}';
//    }
}
