package com.team20.backend.service;

import com.team20.backend.model.user.Avatar;
import com.team20.backend.repository.AvatarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class AvatarServiceImpTest {

    @Mock
    private AvatarRepository avatarRepository;

    @InjectMocks
    private AvatarServiceImp avatarService;

    @Test
    public void findAvatarByUserId_ReturnsAvatars() {
        when(avatarRepository.findAvatarByUserId(1)).thenReturn(Arrays.asList(new Avatar(), new Avatar()));
        List<Avatar> avatars = avatarService.findAvatarByUserId(1);
        assertThat(avatars).hasSize(2);
    }

    @Test
    public void getDefaultAvatar_ReturnsDefaultAvatar() {
        Avatar defaultAvatar = new Avatar();
        defaultAvatar.setAvatarId(2);
        when(avatarRepository.findAvatarByAvatarId(2)).thenReturn(defaultAvatar);
        Avatar result = avatarService.getDefaultAvatar();
        assertThat(result).isEqualTo(defaultAvatar);
    }

    @Test
    public void saveAvatar_PersistsAvatar() {
        Avatar avatar = new Avatar();
        avatarService.saveAvatar(avatar);
        verify(avatarRepository).save(avatar);
    }

    @Test
    public void findByStatus_ReturnsFilteredAvatars() {
        when(avatarRepository.findByStatus("active")).thenReturn(Arrays.asList(new Avatar(), new Avatar()));
        List<Avatar> avatars = avatarService.findByStatus("active");
        assertThat(avatars).hasSize(2);
    }

    @Test
    public void findAvatarById_ReturnsAvatar() {
        Avatar avatar = new Avatar();
        avatar.setAvatarId(1);
        when(avatarRepository.findAvatarByAvatarId(1)).thenReturn(avatar);
        Avatar result = avatarService.findAvatarById(1);
        assertThat(result).isEqualTo(avatar);
    }

    @Test
    public void deleteAvatar_DeletesAvatar() {
        Avatar avatar = new Avatar();
        avatar.setAvatarId(1);
        when(avatarRepository.findAvatarByAvatarId(1)).thenReturn(avatar);
        avatarService.deleteAvatar(1);
        verify(avatarRepository).delete(avatar);
    }

    @Test
    public void findApprovedAvatarByUserId_ReturnsApprovedAvatarOrDefault() {
        // 使用无参数构造函数并设置属性
        Avatar pendingAvatar = new Avatar();
        pendingAvatar.setAvatarId(1);
        pendingAvatar.setStatus("pending");

        Avatar approvedAvatar = new Avatar();
        approvedAvatar.setAvatarId(2);
        approvedAvatar.setStatus("approved");

        List<Avatar> avatars = Arrays.asList(pendingAvatar, approvedAvatar);
        when(avatarRepository.findAvatarByUserId(1)).thenReturn(avatars);

        Avatar defaultAvatar = new Avatar();
        defaultAvatar.setAvatarId(2);
        defaultAvatar.setStatus("default");

        when(avatarRepository.findAvatarByAvatarId(2)).thenReturn(defaultAvatar);

        Avatar result = avatarService.findApprovedAvatarByUserId(1);
        assertThat(result.getStatus()).isEqualTo("approved");

        // 测试没有已批准的头像时返回默认头像
        when(avatarRepository.findAvatarByUserId(1)).thenReturn(Arrays.asList(pendingAvatar));
        result = avatarService.findApprovedAvatarByUserId(1);
        assertThat(result).isEqualTo(defaultAvatar);
    }

}
