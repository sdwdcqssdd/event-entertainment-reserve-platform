package com.team20.backend.repository;


import com.team20.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findBySenderOrReceiver(String sender, String receiver);

    List<ChatMessage> findByReceiverAndSender(String username, String username1);

    List<ChatMessage> findByReceiverAndType(String username, ChatMessage.MessageType chat);

}

