package com.yuvraj.SimplyForge.Service;

import com.yuvraj.SimplyForge.dto.Auth.AuthResponse;
import com.yuvraj.SimplyForge.dto.Auth.LoginRequest;
import com.yuvraj.SimplyForge.dto.Auth.SignupRequest;

public interface AuthService {

    public AuthResponse signup(SignupRequest request);

    public AuthResponse login(LoginRequest request);

    
}
