package com.team20.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team20.backend.model.user.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    public Users findUserByUsername(String username);
    public Users findByEmail(String email);

    Users findUserByUserId(int userId);
}
