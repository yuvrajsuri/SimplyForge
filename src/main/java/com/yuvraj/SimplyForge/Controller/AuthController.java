package com.yuvraj.SimplyForge.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuvraj.SimplyForge.Service.AuthService;
import com.yuvraj.SimplyForge.Service.UserService;
import com.yuvraj.SimplyForge.dto.Auth.AuthResponse;
import com.yuvraj.SimplyForge.dto.Auth.LoginRequest;
import com.yuvraj.SimplyForge.dto.Auth.SignupRequest;
import com.yuvraj.SimplyForge.dto.Auth.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(LoginRequest request){
        Long userId = 1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
