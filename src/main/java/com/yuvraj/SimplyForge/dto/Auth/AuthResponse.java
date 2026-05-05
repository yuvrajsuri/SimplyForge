package com.yuvraj.SimplyForge.dto.Auth;

public record AuthResponse(
    String token, 
    UserProfileResponse user) {

}
