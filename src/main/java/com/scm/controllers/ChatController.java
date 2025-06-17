package com.scm.controllers;

import com.scm.model.ChatMessage;
import com.scm.repsitories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatRepo;

    @GetMapping("/chat")
public String chatPage(Model model) {
    List<String> contacts = List.of("Alice", "Bob", "Charlie");
    List<ChatMessage> allMessages = chatRepo.findAll();
    model.addAttribute("contacts", contacts);
    model.addAttribute("messages", allMessages);
    return "user/chat";  // ✅ correct relative path
}


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return chatRepo.save(message);
    }
}
