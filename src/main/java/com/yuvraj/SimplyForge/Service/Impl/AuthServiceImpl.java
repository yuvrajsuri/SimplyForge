package com.yuvraj.SimplyForge.Service.Impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.AuthService;
import com.yuvraj.SimplyForge.dto.Auth.AuthResponse;
import com.yuvraj.SimplyForge.dto.Auth.LoginRequest;
import com.yuvraj.SimplyForge.dto.Auth.SignupRequest;

@Service
public class AuthServiceImpl implements AuthService{

    @Override
    public AuthResponse signup(SignupRequest request) {
        // AuthenticationManager authenticationManager = 
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signup'");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

}
