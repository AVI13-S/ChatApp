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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final int MAX_USERS = 10;

    private final ChatUserRepository userRepository;
    private final ChatMessageRepository messageRepository;
    private final ChatMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public UserResponse join(UserRequest request) {
        String name = request.username().trim();

        ChatUser user = userRepository.findByUsernameIgnoreCase(name);
        if (user != null) {
            return mapper.toResponse(user);
        }

        if (userRepository.count() >= MAX_USERS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chat room is full, try again later");
        }

        user = userRepository.save(new ChatUser(name));
        return mapper.toResponse(user);
    }

    public void leave(String username) {
        ChatUser user = userRepository.findByUsernameIgnoreCase(username.trim());
        if (user == null) {
            return;
        }
        userRepository.delete(user);
        if (userRepository.count() == 0) {
            messageRepository.deleteAll();
        }
    }

    public List<UserResponse> getUsers() {
        List<UserResponse> users = new ArrayList<>();
        for (ChatUser user : userRepository.findAll()) {
            users.add(mapper.toResponse(user));
        }
        return users;
    }

    public MessageResponse sendMessage(MessageRequest request) {
        ChatUser user = userRepository.findByUsernameIgnoreCase(request.sender().trim());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Join the chat first");
        }

        ChatMessage message = new ChatMessage(user.getUsername(), request.content().trim());
        message = messageRepository.save(message);

        MessageResponse response = mapper.toResponse(message);
        messagingTemplate.convertAndSend("/topic/messages", response);
        return response;
    }

    public List<MessageResponse> getMessages() {
        List<MessageResponse> messages = new ArrayList<>();
        for (ChatMessage message : messageRepository.findAllByOrderBySentAtAsc()) {
            messages.add(mapper.toResponse(message));
        }
        return messages;
    }

    public void deleteMessage(UUID id, String username) {
        if (!messageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }

        ChatMessage message = messageRepository.findById(id).get();
        if (!message.getSender().equalsIgnoreCase(username.trim())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own messages");
        }

        messageRepository.delete(message);

        messagingTemplate.convertAndSend("/topic/deleted", id.toString());
    }
}