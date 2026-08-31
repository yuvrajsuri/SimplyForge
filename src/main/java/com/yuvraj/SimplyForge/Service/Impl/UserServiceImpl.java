package com.yuvraj.SimplyForge.Service.Impl;


import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.UserService;
import com.yuvraj.SimplyForge.dto.Auth.UserProfileResponse;
import com.yuvraj.SimplyForge.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

   UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }

}
