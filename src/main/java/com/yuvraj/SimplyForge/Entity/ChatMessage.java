package com.yuvraj.SimplyForge.Entity;

import java.time.Instant;

import com.yuvraj.SimplyForge.Enums.MessageRole;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ChatMessage {
    Long id;
    ChatSession chatSession;

    String Content;

    MessageRole role;
    String toolCalls;

    Integer tokenUsed;
    Instant createdAt;
}
