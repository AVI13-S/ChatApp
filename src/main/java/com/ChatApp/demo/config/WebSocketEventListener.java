package com.ChatApp.demo.config;

import com.ChatApp.demo.dto.UserRequest;
import com.ChatApp.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatService chatService;
    private final Map<String, String> usernameBySession = new ConcurrentHashMap<>();

    @EventListener
    public void onConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getFirstNativeHeader("username");
        if (username != null) {
            usernameBySession.put(accessor.getSessionId(), username);
        }
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        String username = usernameBySession.remove(event.getSessionId());
        if (username != null) {
            chatService.leave(new UserRequest(username));
        }
    }
}
