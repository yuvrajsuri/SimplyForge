package com.yuvraj.SimplyForge.dto.project;

import java.time.Instant;

import com.yuvraj.SimplyForge.dto.Auth.UserProfileResponse;

public record ProjectResponse(
    long id,
    String name,
    Instant createdAt,
    Instant updatedAt,
    UserProfileResponse owner
) {

}
