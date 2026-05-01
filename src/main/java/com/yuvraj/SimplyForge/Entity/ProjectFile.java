package com.yuvraj.SimplyForge.Entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level=AccessLevel.PRIVATE)
public class ProjectFile {
    Long id;
    Project project;
    String path;
    String MinioObjectKey;
    Instant createdAt;
    Instant updatedAt;

    User createdBy;
    User updatedBy;
}
