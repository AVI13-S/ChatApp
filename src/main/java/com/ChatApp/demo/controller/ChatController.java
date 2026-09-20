package com.ChatApp.demo.controller;

import com.ChatApp.demo.dto.MessageRequest;
import com.ChatApp.demo.dto.MessageResponse;
import com.ChatApp.demo.dto.UserRequest;
import com.ChatApp.demo.dto.UserResponse;
import com.ChatApp.demo.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse join(@Valid @RequestBody UserRequest request) {
        return chatService.join(request);
    }

    @PostMapping("/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leave(@Valid @RequestBody UserRequest request) {
        chatService.leave(request.username());
    }

    @GetMapping("/users")
    public List<UserResponse> users() {
        return chatService.getUsers();
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse sendMessage(@Valid @RequestBody MessageRequest request) {
        return chatService.sendMessage(request);
    }

    @GetMapping("/messages")
    public List<MessageResponse> messages() {
        return chatService.getMessages();
    }

    @DeleteMapping("/messages/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable UUID id, @RequestParam String username) {
        chatService.deleteMessage(id, username);
    }
}