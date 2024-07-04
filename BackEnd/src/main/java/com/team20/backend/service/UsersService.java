package com.team20.backend.service;

import com.team20.backend.model.user.Users;

public interface UsersService {
    void save(Users users);

    Users findUserByUsername(String username);

    Users register(String username, String email, String password);
    Users login(String username, String password);

    Users findUserById(int userId);
}
