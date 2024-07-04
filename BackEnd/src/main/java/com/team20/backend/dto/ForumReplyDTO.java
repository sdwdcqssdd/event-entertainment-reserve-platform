package com.team20.backend.dto;

import com.team20.backend.model.forum.ForumReply;

public class ForumReplyDTO {
    private ForumReply reply;
    private UserDTO user;

    private boolean hasSecondaryReplies;

    public boolean isHasSecondaryReplies() {
        return hasSecondaryReplies;
    }

    public void setHasSecondaryReplies(boolean hasSecondaryReplies) {
        this.hasSecondaryReplies = hasSecondaryReplies;
    }

    public ForumReply getReply() {
        return reply;
    }

    public void setReply(ForumReply reply) {
        this.reply = reply;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ForumReplyDTO(ForumReply reply, UserDTO user) {
        this.reply = reply;
        this.user = user;
    }
}
