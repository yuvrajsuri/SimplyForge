package com.yuvraj.SimplyForge.dto.Auth;

public record UserProfileResponse(
    Long id,
    String email,
    String name,
    String avatarUrl) {

}
