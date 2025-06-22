package com.scm.controllers;

import com.scm.model.ChatMessage;
import com.scm.repsitories.ChatMessageRepository;
import com.scm.repsitories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/chat")
    public String chatPage(Model model, Principal principal) {
        String currentUser = principal.getName();
        List<String> contacts = userRepo.findAllUsernamesExcept(currentUser);
        model.addAttribute("contacts", contacts);
        model.addAttribute("loggedInUser", currentUser);
        return "user/chat";
    }

    @GetMapping("/user/chat/history/{toUser}")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@PathVariable String toUser, Principal principal) {
        String fromUser = principal.getName();
        return chatRepo.findConversation(fromUser, toUser);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return chatRepo.save(message);
    }
}
