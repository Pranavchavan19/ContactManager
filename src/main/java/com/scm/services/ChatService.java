package com.scm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.entities.ChatMessage;
import com.scm.repsitories.ChatMessageRepository;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository messageRepo;




    public ChatMessage saveMessage(ChatMessage message) {
        return messageRepo.save(message);
    }

    public List<ChatMessage> getChatHistory(String user1, String user2) {
        return messageRepo.getChatHistory(user1, user2);
    }
}
