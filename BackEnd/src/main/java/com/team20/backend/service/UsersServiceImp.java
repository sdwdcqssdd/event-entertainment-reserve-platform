package com.team20.backend.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import com.team20.backend.repository.UsersRepository;
import com.team20.backend.model.user.Users;

import java.util.Objects;

@Service
public class UsersServiceImp implements UsersService{
    @Autowired
    private UsersRepository usersRepository;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JavaMailSender javaMailSender;

    public void save(Users users) {
        usersRepository.save(users);
    }

    @Override
    public Users findUserByUsername(String username) {return usersRepository.findUserByUsername(username);}

    @Override
    public Users register(String username, String email, String password) {
        if (usersRepository.findUserByUsername(username) != null) {
            return null;
        } else if (usersRepository.findByEmail(email) != null) {
            return null;
        } else {
            Users users = new Users();
            users.setUsername(username);
            users.setEmail(email);
            users.setPassword(password);
            users.setIdentity(Users.IdentityType.USER);
            usersRepository.save(users);
            return users;
        }
    }

    @Override
    public Users login(String username, String password) {
        Users user = usersRepository.findUserByUsername(username);
        if (user == null) {
            return null;
        } else if (!Objects.equals(user.getPassword(), password)) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public Users findUserById(int userId) {
        return usersRepository.findUserByUserId(userId);
    }
//    public String login(String username, String password) {
//        Users user = usersRepository.findUserByUsername(username);
//        if (user == null) {
//            return "user doesn't exit";
//        } else if (!Objects.equals(user.getPassword(), password)) {
//            return "password is wrong";
//        } else {
//            return "successfully login";
//        }
//    }

}
