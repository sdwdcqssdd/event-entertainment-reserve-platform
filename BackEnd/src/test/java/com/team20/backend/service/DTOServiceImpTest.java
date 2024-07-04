package com.team20.backend.service;

import com.team20.backend.dto.EventCommentDTO;
import com.team20.backend.dto.EventDTO;
import com.team20.backend.dto.UserDTO;
import com.team20.backend.model.Event;
import com.team20.backend.model.history.Comment;
import com.team20.backend.model.user.Avatar;
import com.team20.backend.model.user.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class DTOServiceImpTest {

    @Mock
    private UsersService usersService;
    @Mock
    private EventService eventService;
    @Mock
    private AvatarService avatarService;
    @InjectMocks
    private DTOServiceImp dtoService;
    @Mock
    private CommentService commentService;


    @Test
    public void getUserDTO_ReturnsCorrectlyMappedDTO() {
        // 设置模拟数据
        Users user = new Users();
        user.setUserId(1);
        user.setUsername("John Doe");
        user.setIdentity(Users.IdentityType.USER);

        byte[] avatarData = new byte[]{0x01, 0x02};
        Avatar mockAvatar = mock(Avatar.class);
        when(mockAvatar.getAvatarData()).thenReturn(avatarData);  // 确保模拟的Avatar对象返回正确的头像数据
        when(avatarService.findApprovedAvatarByUserId(1)).thenReturn(mockAvatar);
        when(usersService.findUserById(1)).thenReturn(user);

        // 调用方法
        UserDTO result = dtoService.getUserDTO(1);

        // 断言结果
        assertThat(result.getUserId()).isEqualTo(user.getUserId());
        assertThat(result.getUserName()).isEqualTo(user.getUsername());
        assertThat(result.getAvatarData()).isEqualTo(avatarData);  // 验证头像数据是否匹配预期
        assertThat(result.getIdentity()).isEqualTo(user.getIdentity().toString());
    }
    @Test
    public void getEventDTO_ReturnsCorrectlyMappedDTO() {
        // 模拟事件
        Event event = new Event();
        event.setEventId(1);
        event.setTitle("Sample Event");
        event.setOrganizerId(1);

        // 模拟用户和头像数据
        Users user = new Users();
        user.setUserId(1);
        user.setUsername("John Doe");
        user.setIdentity(Users.IdentityType.USER);

        byte[] avatarData = new byte[]{0x01, 0x02};
        Avatar mockAvatar = mock(Avatar.class);
        UserDTO userDTO = new UserDTO(user, avatarData);

        // 模拟服务响应
        when(mockAvatar.getAvatarData()).thenReturn(avatarData);  // 确保模拟的Avatar对象返回正确的头像数据
        when(eventService.findByEventId(1)).thenReturn(event);
        when(usersService.findUserById(1)).thenReturn(user);
        when(avatarService.findApprovedAvatarByUserId(1)).thenReturn(mockAvatar);

        // 执行方法
        EventDTO result = dtoService.getEventDTO(1);

        // 断言结果
        assertThat(result.getEvent()).isEqualTo(event);
        assertThat(result.getUser()).isEqualToComparingFieldByField(userDTO);
    }
//    @Test
//    public void getEventCommentDTO_ReturnsCorrectlyMappedDTO() {
//        // 创建并模拟事件
//        Event event = new Event();
//        event.setEventId(1);
//        event.setTitle("Sample Event");
//        event.setOrganizerId(1); // 确保 OrganizerId 也被设置
//
//        // 模拟事件服务响应
//        when(eventService.findByEventId(1)).thenReturn(event);
//
//        // 创建用户并模拟
//        Users user = new Users();
//        user.setUserId(1);
//        user.setUsername("John Doe");
//        user.setIdentity(Users.IdentityType.USER);
//
//        // 模拟用户服务响应
//        when(usersService.findUserById(1)).thenReturn(user);
//
//        // 模拟 Avatar 数据
//        byte[] avatarData = new byte[]{0x01, 0x02};
//        Avatar mockAvatar = mock(Avatar.class);
//        when(mockAvatar.getAvatarData()).thenReturn(avatarData);
//        when(avatarService.findApprovedAvatarByUserId(1)).thenReturn(mockAvatar);
//
//        // 创建并模拟用户DTO和事件DTO
//        UserDTO userDTO = new UserDTO(user, avatarData);
//        EventDTO eventDTO = new EventDTO(userDTO, event);
//
//        // 创建并模拟评论列表
//        Comment comment1 = new Comment();
//        comment1.setId(1);
//        comment1.setContent("Great event!");
//        Comment comment2 = new Comment();
//        comment2.setId(2);
//        comment2.setContent("Loved it!");
//        List<Comment> comments = Arrays.asList(comment1, comment2);
//
//        // 模拟评论服务响应
//        when(commentService.findCommentsByEventId(1)).thenReturn(comments);
//        when(dtoService.getEventDTO(1)).thenReturn(eventDTO);
//
//        // 调用方法
//        EventCommentDTO result = dtoService.getEventCommentDTO(1);
//
//        // 断言结果
//        assertThat(result.getEvent()).isEqualTo(eventDTO);
//        assertThat(result.getComments()).containsExactlyInAnyOrderElementsOf(comments);
//    }





}
