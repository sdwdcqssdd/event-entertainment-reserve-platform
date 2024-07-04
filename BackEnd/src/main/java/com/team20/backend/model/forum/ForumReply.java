package com.team20.backend.model.forum;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "forum_reply")
public class ForumReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private ForumTopic forumTopic;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "like_num", nullable = false)
    private int likeNum;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "post_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postTime;

    @Column(name = "parent_id")
    private int parentId;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ForumTopic getForumTopic() {
        return forumTopic;
    }

    public void setForumTopic(ForumTopic forumTopic) {
        this.forumTopic = forumTopic;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
}

