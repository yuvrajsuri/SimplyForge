package com.yuvraj.SimplyForge.dto.member;

import java.time.Instant;

import com.yuvraj.SimplyForge.Enums.ProjectRole;

public record MemberResponse(
    Long userId,
    String email,
    String name,
    String avatarUrl,
    ProjectRole role,
    Instant invitedAt
) {

}
