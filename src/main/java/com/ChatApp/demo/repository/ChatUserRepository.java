package com.ChatApp.demo.repository;

import com.ChatApp.demo.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {

    ChatUser findByUsernameIgnoreCase(String username);
}