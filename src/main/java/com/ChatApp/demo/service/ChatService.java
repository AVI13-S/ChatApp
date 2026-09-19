package com.ChatApp.demo.service;

import com.ChatApp.demo.dto.MessageRequest;
import com.ChatApp.demo.dto.MessageResponse;
import com.ChatApp.demo.dto.UserRequest;
import com.ChatApp.demo.dto.UserResponse;
import com.ChatApp.demo.entity.ChatMessage;
import com.ChatApp.demo.entity.ChatUser;
import com.ChatApp.demo.mapper.ChatMapper;
import com.ChatApp.demo.repository.ChatMessageRepository;
import com.ChatApp.demo.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final int MAX_USERS = 6;

    private final ChatUserRepository userRepository;
    private final ChatMessageRepository messageRepository;
    private final ChatMapper mapper;

    public synchronized UserResponse join(UserRequest request) {
        String username = request.username().trim();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username is already in the chat");
        }
        if (userRepository.count() >= MAX_USERS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "chat room is full (max " + MAX_USERS + " people)");
        }

        ChatUser saved = userRepository.save(new ChatUser(username));
        return mapper.toResponse(saved);
    }

    @Transactional
    public void leave(UserRequest request) {
        userRepository.deleteByUsernameIgnoreCase(request.username().trim());
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public MessageResponse sendMessage(MessageRequest request) {
        String sender = request.sender().trim();

        if (!userRepository.existsByUsernameIgnoreCase(sender)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "join the chat first");
        }

        ChatMessage saved = messageRepository.save(new ChatMessage(sender, request.content().trim()));
        return mapper.toResponse(saved);
    }

    public List<MessageResponse> getLatestMessages() {
        List<ChatMessage> messages = new ArrayList<>(messageRepository.findTop50ByOrderBySentAtDesc());
        Collections.reverse(messages);
        return messages.stream().map(mapper::toResponse).toList();
    }
}