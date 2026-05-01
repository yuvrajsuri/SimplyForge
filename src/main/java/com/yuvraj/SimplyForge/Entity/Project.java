package com.yuvraj.SimplyForge.Entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    Long id;
    String name;
    User owner;
    Boolean isPublic=false;

    Instant createdAt;
    Instant updatedAt;

    Instant deletedAt;
}
