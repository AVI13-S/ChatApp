package com.ChatApp.demo.controller;

import com.ChatApp.demo.dto.MessageRequest;
import com.ChatApp.demo.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void send(@Valid @Payload MessageRequest request) {
        chatService.sendMessage(request);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleError(Exception ex) {
        if (ex instanceof ResponseStatusException statusException) {
            return statusException.getReason();
        }
        return "Invalid message: sender and content are required (content max 1000 characters).";
    }
}
