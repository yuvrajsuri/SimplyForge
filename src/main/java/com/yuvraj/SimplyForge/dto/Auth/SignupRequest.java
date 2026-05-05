package com.yuvraj.SimplyForge.dto.Auth;

public record SignupRequest(
        String email,
        String name,
        String password
) {
}