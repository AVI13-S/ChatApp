package com.ChatApp.demo.mapper;

import com.ChatApp.demo.dto.MessageResponse;
import com.ChatApp.demo.dto.UserResponse;
import com.ChatApp.demo.entity.ChatMessage;
import com.ChatApp.demo.entity.ChatUser;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public UserResponse toResponse(ChatUser user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

    public MessageResponse toResponse(ChatMessage message) {
        return new MessageResponse(message.getId(), message.getSender(), message.getContent(), message.getSentAt());
    }
}