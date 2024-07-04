package com.team20.backend.repository;

import com.team20.backend.model.user.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer>{
    List<Avatar> findAvatarByUserId(int userId);

    Avatar findAvatarByAvatarId(int i);

    List<Avatar> findByStatus(String status);


    //TODO: add custom queries
}
