package com.scm.controllers;

import com.scm.entities.ChatMessage;
import com.scm.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat") // Client sends message to /app/chat
    public void sendMessage(ChatMessage message) {
        // Save to DB
        ChatMessage savedMessage = chatService.saveMessage(message);

        // Send to receiver's topic
        messagingTemplate.convertAndSend("/topic/messages/" + message.getToUser(), savedMessage);
    }
}
