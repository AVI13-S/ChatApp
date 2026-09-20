package com.ChatApp.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        String sender,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant sentAt) {
}