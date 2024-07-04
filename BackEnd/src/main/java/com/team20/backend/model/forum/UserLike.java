package com.team20.backend.model.forum;


import com.team20.backend.model.user.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "user_like")
public class UserLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    private ForumReply reply;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public ForumReply getReply() {
        return reply;
    }

    public void setReply(ForumReply reply) {
        this.reply = reply;
    }
}