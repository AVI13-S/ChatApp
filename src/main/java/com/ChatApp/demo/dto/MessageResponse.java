
package com.ChatApp.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(UUID id, String sender, String content, LocalDateTime sentAt) {
}