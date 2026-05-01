package com.yuvraj.SimplyForge.Entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ChatSession {
    Project project;
    User user;
    String title;

    Instant createdAt;
    Instant updatedAt;

    Instant deletedAt;
}
