package com.yuvraj.SimplyForge.Entity;

import java.time.Instant;

import com.yuvraj.SimplyForge.Enums.PreviewStatus;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level=AccessLevel.PRIVATE)
public class Preview {
    Long id;
    String project;

    String namespace;
    String prodName;
    String previewUrl;

    PreviewStatus status;

    Instant startedAt;
    Instant terminatedAt;

    Instant createdAt;
}
