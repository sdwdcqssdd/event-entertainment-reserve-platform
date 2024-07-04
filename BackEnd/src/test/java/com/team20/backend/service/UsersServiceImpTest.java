package com.team20.backend.service;

import com.team20.backend.model.user.Users;
import com.team20.backend.repository.UsersRepository;
//import com.team20.backend.util.VerifyCodeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



import java.util.concurrent.TimeUnit;
/**
 * AI-generated-content
 * tool: chatGpt
 * version: 4.0
 * usage: gpt根据我给的测试模板为其他方法写测试
 */
@ExtendWith(MockitoExtension.class)
public class UsersServiceImpTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private JavaMailSender javaMailSender;

//    @Mock
//    private VerifyCodeUtil verifyCodeUtil;

    @InjectMocks
    private UsersServiceImp usersService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    public void setUp() {
        lenient().when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

//    @Test
//    public void register_NewUser_ReturnsUser() {
//        when(usersRepository.findUserByUsername(anyString())).thenReturn(null);
//        when(usersRepository.findByEmail(anyString())).thenReturn(null);
//
//        Users newUser = new Users();
//        newUser.setUsername("newuser");
//        newUser.setEmail("newuser@example.com");
//        newUser.setPassword("password123");
//
//        when(usersRepository.save(any(Users.class))).thenReturn(newUser);
//
//        Users result = usersService.register("newuser", "newuser@example.com", "password123", "123456");
//        assertThat(result).isNotNull();
//        assertThat(result.getUsername()).isEqualTo("newuser");
//    }

    @Test
    public void login_ValidCredentials_ReturnsUser() {
        Users existingUser = new Users();
        existingUser.setUsername("user");
        existingUser.setPassword("password");

        when(usersRepository.findUserByUsername("user")).thenReturn(existingUser);

        Users result = usersService.login("user", "password");
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("user");
    }

//    @Test
//    public void verifyEmail_NewEmail_ReturnsSuccess() {
//        when(usersRepository.findByEmail(anyString())).thenReturn(null);
//        when(valueOperations.get(anyString())).thenReturn(null);
//        when(verifyCodeUtil.generateVerifyCode(6)).thenReturn("123456");
//
//        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
//
//        String result = usersService.verifyEmail("newemail@example.com");
//        assertThat(result).isEqualTo("send success");
//        verify(valueOperations).set(eq("newemail@example.com"), eq("123456"), eq(5L), eq(TimeUnit.MINUTES));
//    }

    @Test
    public void save_ShouldPersistUser() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("securePassword");

        usersService.save(user);

        verify(usersRepository).save(user);
    }

    @Test
    public void findUserByUsername_ReturnsUser() {
        Users expectedUser = new Users();
        expectedUser.setUsername("existingUser");

        when(usersRepository.findUserByUsername("existingUser")).thenReturn(expectedUser);

        Users result = usersService.findUserByUsername("existingUser");

        assertThat(result).isEqualTo(expectedUser);
    }

//    @Test
//    public void checkVerifyCode_ReturnsCodeStatus() {
//        when(verifyCodeUtil.checkVerifyCode("email@example.com", "123456")).thenReturn(1);
//
//        int result = usersService.checkVerifyCode("email@example.com", "123456");
//
//        assertThat(result).isEqualTo(1);
//    }

    @Test
    public void findUserById_ReturnsUser() {
        Users expectedUser = new Users();
        expectedUser.setUserId(1);

        when(usersRepository.findUserByUserId(1)).thenReturn(expectedUser);

        Users result = usersService.findUserById(1);

        assertThat(result).isEqualTo(expectedUser);
    }
    // Test registration when username already exists
    @Test
    public void register_UsernameExists_ShouldReturnNull() {
        String username = "testUser";
        String email = "testUser@example.com";
        String password = "password123";

        when(usersRepository.findUserByUsername(username)).thenReturn(new Users());

        Users result = usersService.register(username, email, password);
        assertNull(result);
    }

    // Test registration when email already exists
    @Test
    public void register_EmailExists_ShouldReturnNull() {
        String username = "testUser";
        String email = "testUser@example.com";
        String password = "password123";

        when(usersRepository.findUserByUsername(username)).thenReturn(null);
        when(usersRepository.findByEmail(email)).thenReturn(new Users());

        Users result = usersService.register(username, email, password);
        assertNull(result);
    }

    // Test successful registration
    @Test
    public void register_Successful_ShouldReturnUser() {
        String username = "testUser";
        String email = "testUser@example.com";
        String password = "password123";

        when(usersRepository.findUserByUsername(username)).thenReturn(null);
        when(usersRepository.findByEmail(email)).thenReturn(null);

        Users result = usersService.register(username, email, password);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
    }

    // Test login with non-existent user
    @Test
    public void login_UserNotFound_ShouldReturnNull() {
        String username = "testUser";
        String password = "password123";

        when(usersRepository.findUserByUsername(username)).thenReturn(null);

        Users result = usersService.login(username, password);
        assertNull(result);
    }

    // Test login with incorrect password
    @Test
    public void login_IncorrectPassword_ShouldReturnNull() {
        String username = "testUser";
        String password = "password123";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword("wrongPassword");

        when(usersRepository.findUserByUsername(username)).thenReturn(user);

        Users result = usersService.login(username, password);
        assertNull(result);
    }

    // Test successful login
    @Test
    public void login_Successful_ShouldReturnUser() {
        String username = "testUser";
        String password = "password123";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);

        when(usersRepository.findUserByUsername(username)).thenReturn(user);

        Users result = usersService.login(username, password);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

}
