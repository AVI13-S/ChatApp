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

    // the browser sends its messages to /app/chat.send
    @MessageMapping("/chat.send")
    public void send(@Valid @Payload MessageRequest request) {
        chatService.sendMessage(request);
    }

    // errors go back only to the person who sent the message
    @MessageExceptionHandler(ResponseStatusException.class)
    @SendToUser("/queue/errors")
    public String handleStatusError(ResponseStatusException ex) {
        return ex.getReason();
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleOtherError(Exception ex) {
        return "Message could not be sent";
    }
}