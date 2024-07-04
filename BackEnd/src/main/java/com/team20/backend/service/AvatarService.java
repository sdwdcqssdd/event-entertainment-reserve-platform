package com.team20.backend.service;

import com.team20.backend.model.user.Avatar;

import java.util.List;

public interface AvatarService {
    List<Avatar> findAvatarByUserId(int userId);

    Avatar getDefaultAvatar();

    void saveAvatar(Avatar avatar);

    List<Avatar> findByStatus(String status);

    Avatar findAvatarById(int avatarId);

    void deleteAvatar(int avatarId);

    Avatar findApprovedAvatarByUserId(int organizerId);
}
