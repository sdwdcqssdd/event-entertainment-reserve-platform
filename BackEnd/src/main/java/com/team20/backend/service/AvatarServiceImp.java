package com.team20.backend.service;

import com.team20.backend.model.user.Avatar;
import com.team20.backend.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvatarServiceImp implements AvatarService {
    @Autowired
    private AvatarRepository avatarRepository;

    @Override
    public List<Avatar> findAvatarByUserId(int userId) { return avatarRepository.findAvatarByUserId(userId);}

    @Override
    @Cacheable(value = "DefaultAvatar")
    public Avatar getDefaultAvatar() { return avatarRepository.findAvatarByAvatarId(2);}

    @Override
    public void saveAvatar(Avatar avatar) { avatarRepository.save(avatar); }

    @Override
    public List<Avatar> findByStatus(String status) { return  avatarRepository.findByStatus(status); }

    @Override
    public Avatar findAvatarById(int avatarId) {return avatarRepository.findAvatarByAvatarId(avatarId);}

    @Override
    public void deleteAvatar(int avatarId) {avatarRepository.delete(findAvatarById(avatarId));}

    @Override
    public Avatar findApprovedAvatarByUserId(int organizerId) {
        List<Avatar> avatars = findAvatarByUserId(organizerId);
        for (Avatar avatar : avatars) {
            if (avatar.getStatus().equals("approved")) {
                return avatar;
            }
        }
        return getDefaultAvatar();
    }
}
