package com.scm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.ChatMessage;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ChatService;
import com.scm.services.ContactService;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String chatHome(Model model, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        List<Contact> contacts = contactService.getByUserId(user.getUserId());
        model.addAttribute("contacts", contacts);
        return "user/chat"; // chat.html
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String toUser, @RequestParam String content, Authentication authentication) {
        String fromUser = Helper.getEmailOfLoggedInUser(authentication);
        chatService.saveMessage(new ChatMessage(fromUser, toUser, content));
        return "redirect:/user/chat/" + toUser;
    }

    @GetMapping("/{toUser}")
    public String chatWithContact(@PathVariable String toUser, Model model, Authentication authentication) {
        String fromUser = Helper.getEmailOfLoggedInUser(authentication);
        List<ChatMessage> messages = chatService.getChatHistory(fromUser, toUser);
        model.addAttribute("messages", messages);
        model.addAttribute("toUser", toUser);

        // Load contacts for sidebar
        User user = userService.getUserByEmail(fromUser);
        List<Contact> contacts = contactService.getByUserId(user.getUserId());
        model.addAttribute("contacts", contacts);

        return "user/chat";
    }
}

