
package com.ChatApp.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "must not be empty")
        @Size(max = 30, message = "must be at most 30 characters")
        String username) {
}